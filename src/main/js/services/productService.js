import http from "../http-common";
import http_csv from "../http-csv";

class ProductDataService {
  getAll() {
    return http.get("/products");
  }

  get(code) {
    return http.get(`/products/${code}`);
  }

  create(data) {
    return http.post("/products", data);
  }

  update(code, data) {
    return http.put(`/products/${code}`, data);
  }

  delete(code) {
    return http.delete(`/products/${code}`);
  }

  deleteAll() {
    return http.delete(`/products`);
  }

  findByName(name) {
    return http.get(`/products?name=${name}`);
  }

  uploadFile(file) {
    return http_csv.post("/products/upload", file);
  }

}

export default new ProductDataService();