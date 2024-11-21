package com.example.specommercy.service;
import com.example.specommercy.exception.APIException;
import com.example.specommercy.exception.ResourceNotFoundException;
import com.example.specommercy.model.Category;
import com.example.specommercy.model.Product;
import com.example.specommercy.payload.ProductDTO;
import com.example.specommercy.payload.ProductResponse;
import com.example.specommercy.repository.CategoryRepository;
import com.example.specommercy.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        double specialPrice = productDTO.getPrice() -
                ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());

        Product product = modelMapper.map(productDTO , Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        product.setSpecialPrice(specialPrice);
        product.setProductId(null);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty())
            throw new  APIException("No products created till now.");
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> products = productRepository.findProductByCategory(category);

        if(products.isEmpty())
            throw new APIException("No products found in " + category.getCategoryName() + " category");

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');

        if(products.isEmpty())
            throw new APIException("No products found with name " + keyword);

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
       Product product =  productRepository.findById(productId).
               orElseThrow(() -> new ResourceNotFoundException("Product", "productId",productId));
       String productImage = product.getImage();
       product.setProductName(productDTO.getProductName());
       product.setDescription(productDTO.getDescription());
       product.setQuantity(productDTO.getQuantity());
       product.setDiscount(productDTO.getDiscount());
       product.setPrice(productDTO.getPrice());
        double specialPrice = productDTO.getPrice() -
                ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());
        product.setSpecialPrice(specialPrice);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
        ProductDTO deletedProductDTO = modelMapper.map(product,ProductDTO.class);
        productRepository.delete(product);
        return deletedProductDTO;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));
        String updatedImageName = fileService.uploadImage(path,image);
        product.setImage(updatedImageName);
        return modelMapper.map(productRepository.save(product),ProductDTO.class);
    }
}
