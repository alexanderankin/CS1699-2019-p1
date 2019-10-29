import React, { Component } from 'react';

import { Center } from './utils.jsx';
import events from './events';

export class Home extends Component {
  render() {
    if (!this.props.setup)
    return (
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: 'calc(100vh - 210px)' }}>
        <div style={{ maxWidth: '50%' }}>
          <Center>
            <h4 className="card-title" style={{ margin: '80px 0 80px 0' }}>Card title</h4>
            <button
              type="button"
              className="btn btn-outline-secondary btn-sm"
              style={{ marginBottom: 120 }}
              >
              Choose Files
              </button>
          </Center>
          <div style={{ width: '100%', height: 300 }}>
            <button
              type="button"
              className="btn btn-outline-secondary"
              onClick={() => {
                window.events = events;
                events.emit('setup');
              }}
              >
              Construct Inverted Indicies
            </button>
          </div>
        </div>
      </div>
    );
  }
}
