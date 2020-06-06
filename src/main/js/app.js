import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

import AddProduct from "./components/addProductComponent";
import Product from "./components/productComponent";
import ProductsList from "./components/productsListComponent";
import AddStock from "./components/addStockComponent";
import Stock from "./components/stockComponent";
import StockUpdate from "./components/stockUpdateComponent";
import StocksList from "./components/stocksListComponent";

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <nav className="navbar navbar-expand navbar-dark bg-dark">
            <a href="/" className="navbar-brand">
              Warehouse Inventory System
            </a>
            <div className="navbar-nav mr-auto">
              <li className="nav-item">
                <Link to={"/products"} className="nav-link">
                  Products
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/product/add"} className="nav-link">
                  Add Product
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/stocks"} className="nav-link">
                  Stock Record
                </Link>
              </li>
              <li className="nav-item">
                <Link to={"/stock/add"} className="nav-link">
                  Add Stock Record
                </Link>
              </li>
            </div>
          </nav>

          <div className="container mt-3">
            <Switch>
              <Route exact path={["/", "/products"]} component={ProductsList} />
              <Route exact path="/product/add" component={AddProduct} />
              <Route path="/products/:code" component={Product} />
              <Route exact path={["/stocks"]} component={StocksList} />
              <Route exact path="/stock/add" component={AddStock} />
              <Route path="/stocks/:code/:location" component={Stock} />
              <Route path="/stocks_update/:code/:location" component={StockUpdate} />
            </Switch>
          </div>
        </div>
      </Router>
    );
  }
}

export default App;