import React, { Component } from "react";
import StockDataService from "../services/stockService";

export default class Stock extends Component {
  constructor(props) {
    super(props);
    this.onChangeToLocation = this.onChangeToLocation.bind(this);
    this.onChangeTranQuantity = this.onChangeTranQuantity.bind(this);
    this.getStock = this.getStock.bind(this);
    this.transferStock = this.transferStock.bind(this);

    this.state = {
      currentStock: {
        code: "",
        loction: "",
        quantity: ""
      },

      toLocation: "",
      tranQuantity: "",
      results: [],
      message: ""
    };
  }

  componentDidMount() {
    this.getStock(this.props.match.params.code, this.props.match.params.location);
  }

  onChangeToLocation(e) {
    const toLocation = e.target.value;

    this.setState({
      toLocation: toLocation
    });
  }

  onChangeTranQuantity(e) {
    const tranQuantity = e.target.value;

    this.setState({
      tranQuantity: tranQuantity
    });
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

  transferStock() {
    StockDataService.transfer(
      this.state.currentStock.code,
      this.state.currentStock.location,
      this.state.toLocation,
      this.state.tranQuantity
    )
      .then(response => {
        console.log(response.data);
        this.setState({
          message: "The product was transfered successfully!",
          results: response.data
        });
      })
      .catch(e => {
        console.log(e);
      });
  }

  render() {
    const { currentStock, toLocation, tranQuantity, results  } = this.state;

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
                  <strong>Quantity In Stock:</strong>
                </label>
                {currentStock.quantity}
              </div>
              <div className="form-group">
                <label htmlFor="toLocation">Transfer To</label>
                <input
                  type="text"
                  className="form-control"
                  id="toLocation"
                  value={toLocation}
                  onChange={this.onChangeToLocation}
                />
              </div>
              <div className="form-group">
                <label htmlFor="tranQuantity">Transfer Quantity</label>
                <input
                  type="text"
                  className="form-control"
                  id="tranQuantity"
                  value={tranQuantity}
                  onChange={this.onChangeTranQuantity}
                />
              </div>
            </form>

            <button
              type="submit"
              className="badge badge-success"
              onClick={this.transferStock}
            >
              Transfer
            </button>
            <p>{this.state.message}</p>

            <ul className="list-group">
              {results && 
              	results.map((result, index) => (
              	<li className="list-group-item">
              		{"Location: " + result.location + "	"}
              		{"Product Code: " + result.code + "	"}
              		{"Quantity: " + result.quantity + "	"}
              	</li>
              ))}
            </ul>
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