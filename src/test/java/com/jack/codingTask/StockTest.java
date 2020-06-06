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

import com.jack.codingTask.model.Stock;
import com.jack.codingTask.model.StockKey;
import com.jack.codingTask.repository.StockRepository;
import com.jack.codingTask.model.Product;
import com.jack.codingTask.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StockTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    private HttpHeaders httpHeaders;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
    }

    @After
    public void clear() {
        productRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Test
    public void testCreateStock() throws Exception {
        JSONObject request = new JSONObject();
        request.put("code", "FM-HKTV01");
        request.put("location", "TKO");
        request.put("quantity", 100);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders
                        .post("/stocks")
                        .headers(httpHeaders)
                        .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(request.getString("code")))
                .andExpect(jsonPath("$.location").value(request.getString("location")))
                .andExpect(jsonPath("$.quantity").value(request.getInt("quantity")))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"));
    }

    @Test
    public void testGetStock() throws Exception {
        Product product = createProduct("JA-HKTV02", "JAVA", 450);
        productRepository.save(product);

        mockMvc.perform(get("/products/" + product.getCode())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.weight").value(product.getWeight()));

        Stock stock = createStock("TKO", product.getCode(), 450);
        stockRepository.save(stock);

        mockMvc.perform(get("/stocks/" + stock.getCode() + "/" + stock.getLocation())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(stock.getLocation()))
                .andExpect(jsonPath("$.code").value(stock.getCode()))
                .andExpect(jsonPath("$.quantity").value(stock.getQuantity()));

    }

    @Test
    public void testUpdateStockQuantity() throws Exception {
        Product product = createProduct("JA-HKTV02", "JAVA", 450);
        productRepository.save(product);

        Stock stock = createStock("TKO", product.getCode(), 450);
        stockRepository.save(stock);

        JSONObject request = new JSONObject();
        request.put("location", "TKO");
        request.put("code", product.getCode());
        request.put("quantity", 550);

        mockMvc.perform(put("/stocks/updateQt/" + stock.getCode() + "/" + stock.getLocation())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(stock.getCode()))
                .andExpect(jsonPath("$.code").value(stock.getLocation()))
                .andExpect(jsonPath("$.quantity").value(request.getInt("quantity")));
    }

    @Test
    public void testTransfer() throws Exception {
        Product product = createProduct("JA-HKTV02", "JAVA", 450);
        productRepository.save(product);

        mockMvc.perform(get("/products/" + product.getCode())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.weight").value(product.getWeight()));

        Stock stock1 = createStock("TKO", product.getCode(), 450);
        stockRepository.save(stock1);

        mockMvc.perform(get("/stocks/" + stock1.getCode() + "/" + stock1.getLocation())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(stock1.getLocation()))
                .andExpect(jsonPath("$.code").value(stock1.getCode()))
                .andExpect(jsonPath("$.quantity").value(stock1.getQuantity()));

        Stock stock2 = createStock("CWB", product.getCode(), 450);
        stockRepository.save(stock2);

        mockMvc.perform(get("/stocks/" + stock2.getCode() + "/" + stock2.getLocation())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(stock2.getLocation()))
                .andExpect(jsonPath("$.code").value(stock2.getCode()))
                .andExpect(jsonPath("$.quantity").value(stock2.getQuantity()));

        MvcResult result = mockMvc.perform(put("/stocks/" + stock1.getCode() + "/"
        		+ stock1.getLocation() + "/" + stock2.getLocation() + "/" + 1)
        		.headers(httpHeaders))
        		.andReturn();

        MockHttpServletResponse mockHttpResponse = result.getResponse();
        String responseJSONStr = mockHttpResponse.getContentAsString();
        JSONArray stockJSONArray = new JSONArray(responseJSONStr);

        List<String> stockQuantities = new ArrayList<>();
        for (int i = 0; i < stockJSONArray.length(); i++) {
            JSONObject stockJSON = stockJSONArray.getJSONObject(i);
            stockQuantities.add(stockJSON.getString("quantity"));
        }

        Assert.assertEquals("449", stockQuantities.get(0));
        Assert.assertEquals("451", stockQuantities.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception {
        Product product = createProduct("CC-HKTV04", "C++", 450);
        productRepository.save(product);

        Stock stock = createStock("TKO", product.getCode(), 450);
        stockRepository.save(stock);

        mockMvc.perform(delete("/stocks/" + stock.getCode() + "/" + stock.getLocation())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());

        StockKey stockKey = new StockKey(stock.getLocation(), stock.getCode());
        stockRepository.findById(stockKey)
                .orElseThrow(RuntimeException::new);
    }

    private Product createProduct(String code, String name, long weight) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setWeight(weight);

        return product;
    }

    private Stock createStock(String location, String code, long quantity) {
        Stock stock = new Stock();
        stock.setLocation(location);
        stock.setCode(code);
        stock.setQuantity(quantity);

        return stock;
    }
}