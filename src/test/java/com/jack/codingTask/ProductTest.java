package com.jack.codingTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jack.codingTask.model.Product;
import com.jack.codingTask.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private HttpHeaders httpHeaders;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
    }

    @After
    public void clear() {
        productRepository.deleteAll();
    }

    @Test
    public void testCreateProduct() throws Exception {
        JSONObject request = new JSONObject();
        request.put("code", "FM-HKTV01");
        request.put("name", "face mask");
        request.put("weight", 100);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders
                        .post("/products")
                        .headers(httpHeaders)
                        .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(request.getString("code")))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.weight").value(request.getInt("weight")))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    public void testGetProduct() throws Exception {
        Product product = createProduct("JA-HKTV02", "JAVA", 450);
        productRepository.save(product);

        mockMvc.perform(get("/products/" + product.getCode())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.weight").value(product.getWeight()));
    }

    @Test
    public void testReplaceProduct() throws Exception {
        Product product = createProduct("PY-HKTV03", "Python", 450);
        productRepository.save(product);

        JSONObject request = new JSONObject();
        request.put("name", "ABCDE");
        request.put("price", 550);

        mockMvc.perform(put("/products/" + product.getCode())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.weight").value(request.getInt("weight")));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception {
        Product product = createProduct("CC-HKTV04", "C++", 450);
        productRepository.save(product);

        mockMvc.perform(delete("/products/" + product.getCode())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());

        productRepository.findById(product.getCode())
                .orElseThrow(RuntimeException::new);
    }


    private Product createProduct(String code, String name, long weight) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setWeight(weight);

        return product;
    }
}
