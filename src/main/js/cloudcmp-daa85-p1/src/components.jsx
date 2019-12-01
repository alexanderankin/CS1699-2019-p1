import React, { Component } from 'react';

import utils from './myutils.jsx';
import events from './events';


/**
 * back button, heading, and rest of page.
 */
export class Query extends Component {
  render() {
    console.log(this.props);
    return <div className="container">
      <div className="row">
        <div className="col">
          <div className="float-right">
            <button type="button" className="btn btn-info" onClick={() => this.props.change('menu')}>Go Back To Search</button>
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col">
          <h3>{this.props.queryTitle}</h3>
        </div>
      </div>
      <div className="row">
        <div className="col">
          {this.props.children}
        </div>
      </div>
    </div>
  }
}

export class Search extends Component {
  constructor(props) {
    super(props);
    this.state = {
      submitted: false,
      word: '',
      loading: false,
      resultString: '',
      error: false
    };

    this.get = this.get.bind(this);
  }

  async get() {
    this.setState({ submitted: true, loading: true });
    var response = await fetch('/word?queryWord=' + this.state.word);
    var { error, success, data } = await response.json();
    if (error) {
      this.setState({ loading: false, error });
    } else if (success) {
      try {
        this.setState({ loading: false, resultString: atob(data), error: false });
      } catch (e) {
        console.log(e);
        this.setState({ loading: false, error: 'Got Data ' + data + ' which was improperly formatted' });
      }
    }
  }

  render() {
    if (!this.state.submitted) {
      return <div className="w-75 m-auto text-center">
        <h3 className="mb-5">Enter Your Search Term</h3>
        <div className="w-50 m-auto">
          <form onSubmit={(e) => { e.preventDefault(); this.get(); }}>
            <input type="text" className="form-control mb-5" placeholder="Type Your Search Here"
              value={this.state.word}
              onChange={e => { var word = e.target.value; this.setState({ word }); }}
              />
            <button type="submit" className="btn btn-info btn-lg">Search</button>
          </form>
        </div>
      </div>;
    }
    if (this.state.loading) {
      return <p>loading</p>;
    }
    var results = this.state.resultString.split(",");
    results = results.map(function separate(docInfo) {
      return docInfo.split("_");
    });
    results.sort((a, b) => { return parseInt(b[1], 10) - parseInt(a[1], 10); });
    var props = Object.assign({}, this.props);
    console.log("not loading and submitted, error:", this.state.error);
    return <Query {...props} >
      {this.state.error
        ? <div className="text-center">
            <h3 className="card-title">Error</h3>
            <p className='cart-text'>{this.state.message}</p>
            <button type="button" className="btn btn-info" onClick={() => this.setState({ error: '', submitted: false })}>Try Again</button>
          </div>
        : <table className="table table-bordered table-inverse table-hover">
            <thead>
              <tr>
                <th width="50%">Doc Name</th>
                <th width="50%">Frequencies</th>
              </tr>
            </thead>
            <tbody>
              {results.map((r, i) => {
                  {/*return <pre>{r[0]}</pre>*/}
                return <tr key={i}>
                  {r.map((td, j) => {
                    return <td key={j}>{td}</td>;
                  })}
                </tr>;
              })}
            </tbody>
          </table> }
    </Query>
  }
}

export class Top extends Component {
  constructor(props) {
    super(props);
    this.state = {
      submitted: false,
      top: '',
      loading: false,
      resultString: '',
      error: false
    };

    this.get = this.get.bind(this);
  }

  async get() {
    this.setState({ submitted: true, loading: true });
    var response = await fetch('/top?queryTop=' + this.state.top);
    var body = await response.json();
    console.log("i got back a json body", body);
    var { error, success, data } = body;
    if (error) {
      this.setState({ loading: false, error });
    } else if (success) {
      try {
        this.setState({ loading: false, resultString: atob(data), error: false });
      } catch (e) {
        console.log(e);
        this.setState({ loading: false, error: 'Got Data ' + data + ' which was improperly formatted' });
      }
    }
  }

  render() {
    if (!this.state.submitted) {
      return <div className="w-75 m-auto text-center">
        <h3 className="mb-5">Enter Your N Value</h3>
        <div className="w-50 m-auto">
          <form onSubmit={(e) => { e.preventDefault(); this.get(); }}>
            <input type="text" className="form-control mb-5" placeholder="Enter Your N Value"
              value={this.state.top}
              onChange={e => { var top = e.target.value; this.setState({ top }); }}
              />
            <button type="submit" className="btn btn-info btn-lg">Search</button>
          </form>
        </div>
      </div>;
    }
    if (this.state.loading) {
      return <p>loading</p>;
    }
    var results = this.state.resultString.split("\n");
    results = results.map(function separate(docInfo) {
      return docInfo.split("\t");
    });
    results.sort((a, b) => { return parseInt(b[1], 10) - parseInt(a[1], 10); });
    var props = Object.assign({}, this.props);
    console.log("not loading and submitted, error:", this.state.error);
    return <Query {...props} >
      {this.state.error
        ? <div className="text-center">
            <h3 className="card-title">Error</h3>
            <p className='cart-text'>{this.state.message}</p>
            <button type="button" className="btn btn-info" onClick={() => this.setState({ error: '', submitted: false })}>Try Again</button>
          </div>
        : <table className="table table-bordered table-inverse table-hover">
            <thead>
              <tr>
                <th width="50%">Term</th>
                <th width="50%">Total Frequencies</th>
              </tr>
            </thead>
            <tbody>
              {results.map((r, i) => {
                  {/*return <pre>{r[0]}</pre>*/}
                return <tr key={i}>
                  {r.map((td, j) => {
                    return <td key={j}>{td}</td>;
                  })}
                </tr>;
              })}
            </tbody>
          </table> }
    </Query>
  }
}

export class Menu extends Component {
  render() {
    return <div>
      <div className="text-center">
        <h4 className="card-title">Engine was loaded & Indicies constructed</h4>
      </div>

      <div className="text-left">
      </div>
      <div className="text-center">
        <p className="card-text mt-5 mb-5">Please select action</p>
        <div><button onClick={() => this.props.change('search')} type="button" className="btn btn-outline-secondary mb-2">Search for Term</button></div>
        <div><button onClick={() => this.props.change('top')} type="button" className="btn btn-outline-secondary mb-2">Top-N</button></div>
      </div>
    </div>;
  }
}

var loadedMap = {
  'menu': Menu,
  'search': Search,
  'top': Top
};

export class Loaded extends Component {
  constructor(props) {
    super(props);
    this.state = {
      state: 'menu'
    };
  }

  render() {
    var s = (state) => this.setState({ state });
    var Item = loadedMap[this.state.state];
    return <Item change={s}/>;
  }
}

export class Initial extends Component {
  constructor(props) {
    super(props);
    this.onUploadStatusChange = this.onUploadStatusChange.bind(this);
    this.onClickConstruct     = this.onClickConstruct.bind(this);
    this.state = { uploaded: false, formRef: null, loading: false };
    events.on('mainloading', (loading) => this.setState({ loading }));
  }

  onUploadStatusChange(status, formRef) {
    this.setState({ uploaded: status, formRef });
  }

  async onClickConstruct() {
    events.emit('mainloading', true);
    var data = new FormData(this.state.formRef);
    var response = await fetch('/upload', {
      method: 'post',
      body: data
    });
    var status = await response.json();
    events.emit('mainloading', false);
    if (status.error) {
      this.setState({ message: 'Try again' })
    } else {
      this.setState({ message: '' })
      events.emit('setup');
    }
  }

  render() {
    var FileInput = utils.FileInput;
    return (
      <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: 'calc(100vh - 210px)' }}>
        <div style={{textAlign: 'center', alignItems: 'center'}}>
        <div style={{ display: this.state.loading ? 'block': 'none' }}>
          <i className="spin fa fa-spinner"></i>
        </div>
        <div style={{ display: this.state.loading ? 'none': 'block' }}>
          <h4 className="card-title">Load Engine</h4>
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
              <div className="alert alert-warning" role="alert">
                <strong>Error!</strong>
                <div><p>{this.state.message}</p></div>
                <div>
                  <button
                    type="button"
                    className="btn btn-outline-dark btn-sm"
                    onClick={() => this.setState({ message: '' })}>
                    Got it
                  </button>
                </div>
              </div>
            </div> : null}
        </div>
        </div>
      </div>
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
