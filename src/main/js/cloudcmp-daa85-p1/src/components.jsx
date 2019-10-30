import React, { Component } from 'react';

import utils from './myutils.jsx';
import events from './events';

export class Loaded extends Component {
  render() {
    return <div>
      <div className="text-center">
        <h4 className="card-title">Engine was loaded & Indicies constructed</h4>
      </div>

      <div className="text-left">
      </div>
      <div className="text-center">
        <p className="card-text mt-5 mb-5">Please select action</p>
        <div><button type="button" class="btn btn-outline-secondary mb-2">Search for Term</button></div>
        <div><button type="button" class="btn btn-outline-secondary mb-2">Top-N</button></div>
      </div>
    </div>;
  }
}

export class Initial extends Component {
  constructor(props) {
    super(props);
    this.onUploadStatusChange = this.onUploadStatusChange.bind(this);
    this.onClickConstruct     = this.onClickConstruct.bind(this);
    this.state = { uploaded: false, formRef: null };
  }

  onUploadStatusChange(status, formRef) {
    this.setState({ uploaded: status, formRef });
  }

  async onClickConstruct() {
    var data = new FormData(this.state.formRef);
    var status = 'error';
    if (status.indexOf('error') > -1) {
      this.setState({ message: 'Try again' })
    } else {
      events.emit('setup');
    }
  }

  render() {
    var FileInput = utils.FileInput;
    return (
      <div>
        <h4 className="card-title">Card title</h4>
        <FileInput onUploadStatusChange={this.onUploadStatusChange}></FileInput>
        <div style={{ flexBasis: '100%' }}></div>
        <button
          type="button"
          className="btn btn-outline-secondary w-f-c"
          style={{ margin: '0 auto'}}
          onClick={this.onClickConstruct}
          disabled={!this.state.uploaded}
          >
          Construct Inverted Indicies
        </button>
        {this.state.message ? <div className="mt-2">
            <div class="alert alert-warning" role="alert">
              <strong>Error!</strong>
              <div><p>{this.state.message}</p></div>
              <div>
                <button
                  type="button"
                  class="btn btn-outline-dark btn-sm"
                  onClick={() => this.setState({ message: '' })}>
                  Got it
                </button>
              </div>
            </div>
          </div> : null}
      </div>
    );
  }
}

export class Home extends Component {
  render() {
    if (!this.props.state.setup)
      return <Initial></Initial>
    return <Loaded></Loaded>;
  }
}
