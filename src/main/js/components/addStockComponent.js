import React, { Component } from "react";
import StockDataService from "../services/stockService";

export default class AddStock extends Component {
  constructor(props) {
    super(props);
    this.onChangeCode = this.onChangeCode.bind(this);
    this.onChangeLocation = this.onChangeLocation.bind(this);
    this.onChangeQuantity = this.onChangeQuantity.bind(this);
    this.saveStock = this.saveStock.bind(this);
    this.newStock = this.newStock.bind(this);

    this.state = {
      code: "",
      location: "",
      quantity: "",

      submitted: false
    };
  }

  onChangeCode(e) {
    this.setState({
      code: e.target.value,
    });
  }

  onChangeLocation(e) {
    this.setState({
      location: e.target.value
    });
  }

  onChangeQuantity(e) {
    this.setState({
      quantity: e.target.value
    });
  }

  saveStock() {
    var data = {
      location: this.state.location,
      code: this.state.code,
      quantity: this.state.quantity
    };

    StockDataService.create(data)
      .then(response => {
        this.setState({
          location: response.data.location,
          code: response.data.code,
          quantity: response.data.quantity,

          submitted: true
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  newStock() {
    this.setState({
      code: "",
      location: "",
      quantity: "",

      submitted: false
    });
  }

  render() {
    return (
      <div className="submit-form">
        {this.state.submitted ? (
          <div>
            <h4>You submitted successfully!</h4>
            <button className="btn btn-success" onClick={this.newStock}>
              Add
            </button>
          </div>
        ) : (
          <div>
            <div className="form-group">
              <label htmlFor="code">Product Code</label>
              <input
                type="text"
                className="form-control"
                id="code"
                required
                value={this.state.code}
                onChange={this.onChangeCode}
                name="code"
              />
            </div>

            <div className="form-group">
              <label htmlFor="location">Location</label>
              <input
                type="text"
                className="form-control"
                id="location"
                required
                value={this.state.location}
                onChange={this.onChangeLocation}
                name="location"
              />
            </div>

            <div className="form-group">
              <label htmlFor="quantity">Quantity</label>
              <input
                type="text"
                className="form-control"
                id="quantity"
                required
                value={this.state.quantity}
                onChange={this.onChangeQuantity}
                name="quantity"
              />
            </div>

            <button onClick={this.saveStock} className="btn btn-success">
              Submit
            </button>
          </div>
        )}
      </div>
    );
  }
}