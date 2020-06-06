import http from "../http-common";
import http_csv from "../http-csv";

class StockDataService {
  getAll() {
    return http.get("/stocks");
  }

  get(code, location) {
    return http.get(`/stocks/${code}/${location}`);
  }

  create(data) {
    return http.post("/stocks", data);
  }

  transfer(code, from, to, quantity) {
    return http.put(`/stocks/${code}/${from}/${to}/${quantity}`);
  }

  updateQT(code, location, data) {
    return http.put(`/stocks/updateQt/${code}/${location}`, data);
  }

  delete(code, location) {
    return http.delete(`/stocks/${code}/${location}`);
  }

  deleteAll() {
    return http.delete(`/stocks`);
  }

  findByCode(code) {
    return http.get(`/stocks?code=${code}`);
  }

  uploadFile(file) {
    return http_csv.post("/stocks/upload", file);
  }

}

export default new StockDataService();