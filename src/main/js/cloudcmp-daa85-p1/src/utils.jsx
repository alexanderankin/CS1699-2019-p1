import React from 'react';

export function Center(props) {
  return (
    <div style={{ width: '100%' }}>
      <div className="fit-content-class">
        {props.children}
      </div>
    </div>
  );
}

export function Wrap(props) {
  return (
    <div className={props.classNames}>
      {props.children}
    </div>
  );
}
