import React, { Component } from "react";
import StockDataService from "../services/stockService";

export default class StockUpdate extends Component {
  constructor(props) {
    super(props);
    this.onChangeQuantity = this.onChangeQuantity.bind(this);
    this.getStock = this.getStock.bind(this);
    this.deleteStock = this.deleteStock.bind(this);
    this.updateStock = this.updateStock.bind(this);

    this.state = {
      currentStock: {
        code: "",
        loction: "",
        quantity: ""
      },
      message: ""
    };
  }

  componentDidMount() {
    this.getStock(this.props.match.params.code, this.props.match.params.location);
  }

  getStock(code, location) {
    StockDataService.get(code, location)
      .then(response => {
        this.setState({
          currentStock: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  onChangeQuantity(e) {
    const quantity = e.target.value;
    
    this.setState(prevState => ({
      currentStock: {
        ...prevState.currentStock,
        quantity: quantity
      }
    }));
  }

  deleteStock() {    
    StockDataService.delete(this.state.currentStock.code, this.state.currentStock.location)
      .then(response => {
        console.log(response.data);
        this.props.history.push('/stocks')
      })
      .catch(e => {
        console.log(e);
      });
  }

  updateStock() {
    StockDataService.updateQT(this.state.currentStock.code, 
      this.state.currentStock.location, this.state.currentStock)
      .then(response => {
        console.log(response.data);
        this.setState({
          message: "The stock was updated successfully!"
        });
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    const { currentStock } = this.state;

    return (
      <div>
        {currentStock ? (
          <div className="edit-form">
            <h4>Stock Record</h4>
            <form>
              <div className="form-group">
                <label>
                  <strong>Location:</strong>
                </label>
                {currentStock.location}
              </div>
              <div className="form-group">
                <label>
                  <strong>Product Code:</strong>
                </label>
                {currentStock.code}
              </div>
              <div className="form-group">
                <label>
                  <strong>Current Quantity In Stock:</strong>
                </label>
                {currentStock.quantity}
              </div>
              <div className="form-group">
                <label htmlFor="newQuantity">New Quantity In Stock</label>
                <input
                  type="text"
                  className="form-control"
                  id="newQuantity"
                  value={currentStock.quantity}
                  onChange={this.onChangeQuantity}
                />
              </div>
            </form>

            <button
              className="badge badge-danger mr-2"
              onClick={this.deleteStock}
            >
              Delete
            </button>

            <button
              type="submit"
              className="badge badge-success"
              onClick={this.updateStock}
            >
              Update
            </button>
            <p>{this.state.message}</p>
          </div>
        ) : (
          <div>
            <br />
            <p>Please click on a Stock Record...</p>
          </div>
        )}
      </div>
    );
  }
}