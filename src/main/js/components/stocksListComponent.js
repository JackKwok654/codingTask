import React, { Component } from "react";
import StockDataService from "../services/stockService";
import { Link } from "react-router-dom";
import axios from "axios";

export default class StocksList extends Component {
  constructor(props) {
    super(props);
    this.onChangeSearchCode = this.onChangeSearchCode.bind(this);
    this.retrieveStocks = this.retrieveStocks.bind(this);
    this.refreshList = this.refreshList.bind(this);
    this.setActiveStock = this.setActiveStock.bind(this);
    this.removeAllStocks = this.removeAllStocks.bind(this);
    this.searchCode = this.searchCode.bind(this);
    this.downloadCSV = this.downloadCSV.bind(this);
    this.uploadCSV = this.uploadCSV.bind(this);

    this.state = {
      stocks: [],
      currentStock: null,
      currentIndex: -1,
      searchCode: ""
    };
  }

  componentDidMount() {
    this.retrieveStocks();
  }

  onChangeSearchCode(e) {
    const searchCode = e.target.value;

    this.setState({
      searchCode: searchCode
    });
  }

  retrieveStocks() {
    StockDataService.getAll()
      .then(response => {
        this.setState({
          stocks: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  refreshList() {
    this.retrieveStocks();
    this.setState({
      currentStock: null,
      currentIndex: -1
    });
  }

  setActiveStock(stock, index) {
    this.setState({
      currentStock: stock,
      currentIndex: index
    });
  }

  removeAllStocks() {
    StockDataService.deleteAll()
      .then(response => {
        console.log(response.data);
        this.refreshList();
      })
      .catch(e => {
        console.log(e);
      });
  }

  searchCode() {
    StockDataService.findByCode(this.state.searchCode)
      .then(response => {
        this.setState({
          stocks: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  downloadCSV() {
    axios({
      url: 'http://localhost:8080/api/stocks/download',
      method: 'GET',
      responseType: 'blob',
    }).then((response) => {
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'stocks.csv');
      document.body.appendChild(link);
      link.click();
    });
  }

  uploadCSV(e) {
    console.log("Uploading file", event.target.files[0]);
    const formData = new FormData();
    formData.append('file', event.target.files[0]);

    StockDataService.uploadFile(formData)
    .then(response => {
        console.log(response.data);
        this.refreshList();
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    const { searchCode, stocks, currentStock, currentIndex } = this.state;

    return (
      <div className="list row">
        <div className="col-md-8">
          <div className="input-group mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Search by product's code"
              value={searchCode}
              onChange={this.onChangeSearchCode}
            />
            <div className="input-group-append">
              <button
                className="btn btn-outline-secondary"
                type="button"
                onClick={this.searchCode}
              >
                Search
              </button>
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <h4>Stocks List</h4>

          <ul className="list-group">
            {stocks &&
              stocks.map((stock, index) => (
                <li
                  className={
                    "list-group-item " +
                    (index === currentIndex ? "active" : "")
                  }
                  onClick={() => this.setActiveStock(stock, index)}
                  key={index}
                >
                  {"Product Code: " + stock.code + "	"}
                  {"Location: " + stock.location + "	"}
                  {"Quantity: " + stock.quantity + "	"}
                </li>
              ))}
          </ul>

          <button
            className="m-3 btn btn-sm btn-success"
            onClick={this.downloadCSV}
          >
            Download stocks.csv
          </button>

          <button
            className="m-3 btn btn-sm btn-danger"
            onClick={this.removeAllStocks}
          >
            Remove All
          </button>

           <div className="container">
              <div className="row">
                  <div className="col-md-6">
                          <div>
                              <label>Upload Your File </label>
                              <input type="file" accept=".csv" className="form-control" name="file" onChange={this.uploadCSV}/>
                          </div>
                  </div>
              </div>
          </div>

        </div>
        <div className="col-md-6">
          {currentStock ? (
            <div>
              <h4>Stock</h4>
              <div>
                <label>
                  <strong>Product Code:</strong>
                </label>{" "}
                {currentStock.code}
              </div>
              <div>
                <label>
                  <strong>Location:</strong>
                </label>{" "}
                {currentStock.location}
              </div>
              <div>
                <label>
                  <strong>Quantity:</strong>
                </label>{" "}
                {currentStock.quantity}
              </div>

              <Link
                to={"/stocks_update/" + currentStock.code + "/" + currentStock.location}
                className="badge badge-warning"
              >
                Update
              </Link>

              <Link
                to={"/stocks/" + currentStock.code + "/" + currentStock.location}
                className="badge badge-success"
              >
                Transfer
              </Link>
            </div>
          ) : (
            <div>
              <br />
              <p>Please click on a Stock...</p>
            </div>
          )}
        </div>
      </div>
    );
  }
}