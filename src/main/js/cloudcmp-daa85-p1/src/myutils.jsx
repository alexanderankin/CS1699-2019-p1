import React, { Component } from 'react';

import md5 from 'md5';
import moment from 'moment';

class FileInput extends Component {
  constructor(props) {
    super(props);
    this.onChange = this.onChange.bind(this);
    this.inputRef = React.createRef();
    this.formRef = React.createRef();
    this.state = { files: [], error: '' };
  }

  areFilesOk(files) {
    var length = files.length;
    for (var i = 0; i < length; i++) {
      if (files[i].type !== 'application/gzip') return false;
    }
    return true;
  }

  async onChange(e) {
    // set previous files dirty
    this.props.onUploadStatusChange(false);
    var target = e.target;
    var files = target.files;

    // complain if wrong format
    if (!this.areFilesOk(files)) {
      this.formRef.current.reset();  // reset the form, empty out files
      this.setState({ error: 'Files must be tarballs.' });
      return;
    }

    // read files
    var length = files.length;
    var filePromises = [];
    for (var i = 0; i < length; i++) {
      filePromises.push(new Promise((resolve, reject) => {
        var file = files[i];
        var { lastModified, lastModifiedDate, name, size, type } = file;
        var reader = new FileReader();
        reader.onload = (e) => {
          resolve({
            contents: e.target.result,
            sum: md5(e.target.result),
            lastModified, lastModifiedDate, name, size, type
          });
        };
        reader.readAsBinaryString(file);
      }));
    }

    // give info to uploader and display
    var fileReads = await Promise.all(filePromises);
    this.setState({ files: fileReads });
    this.props.onUploadStatusChange(true, this.formRef.current);
  }

  render() {
    var resetError = () => { this.setState({ error: '', files: [] }) };
    if (this.state.error)
      return <div>
        <h3 className="card-title">Error</h3>
        <p className="card-text">{this.state.error}</p>
        <button type="button" className="btn btn-warning mb-3" onClick={resetError}>Try Again</button>
      </div>;

    return <div>
      <button
        type="button"
        className="btn btn-outline-secondary btn-sm mt-4"
        style={{ marginBottom: (this.state.files.length ? 30 : 120) }}
        onClick={() => this.inputRef.current.click()}
        >
        Choose Files
        </button>
      <ul className="list-unstyled text-left">
        {this.state.files.map(f => {
          return <li className="mb-1" key={f.sum}>
            <i className="fa fa-file-o m-1"></i>
            { ' ' }
            {f.name + ' (' + f.size + '), from ' + moment(f.lastModifiedDate).format('lll') }
          </li>
        })}
      </ul>
      <form onSubmit={this.onSubmit} ref={this.formRef}>
        <input
          ref={this.inputRef}
          className="d-none"
          name="docs"
          type="file"
          multiple
          onChange={this.onChange}
          />
      </form>
    </div>;
  }
}

var exports = {
  FileInput
};

export default exports;
