package com.lcb.ecommercebackend.project.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class ProductSupplierActivity {
    @JsonProperty("products")
    public List<Products> products;
    @JsonProperty("supplier_id")
    public String supplierId;

    @Data
    public static class Products{
        @JsonProperty("product_id")
        public String productId;

        @JsonProperty("status")
        public Boolean status;
    }
}
