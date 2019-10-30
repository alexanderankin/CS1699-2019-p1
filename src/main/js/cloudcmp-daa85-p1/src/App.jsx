import React, { Component } from 'react';
import './App.css';

// import { Center, Wrap } from './utils.jsx';
import { Home } from './components.jsx';

import events from './events';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = { setup: false };
  }

  componentDidMount() {
    console.log("listening on setup");
    events.on('setup', () => { console.log('this.setState({ setup: true })') });
    events.on('setup', () => { this.setState({ setup: true }) });
  }

  render() {
    return (
      <div className="container pt-5">
        <div className="row">
          <div className="col">
            <div className="card">
              <div className="card-body">
                <p>Daa85 Search Engine</p>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: 'calc(100vh - 210px)' }}>
                  <div style={{textAlign: 'center', alignItems: 'center'}}>
                  <Home state={this.state}></Home>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
