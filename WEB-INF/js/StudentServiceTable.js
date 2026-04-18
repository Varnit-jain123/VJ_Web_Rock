// VJWebRock Auto-Generated Proxy (StudentServiceTable)

class StudentServiceTable {
  update(data) {
    let url = 'schoolService/studentTable/update';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

  getAll(data) {
    let url = 'schoolService/studentTable/getAll';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.json());
  }

  getBy(data) {
    let url = 'schoolService/studentTable/getBy';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.json());
  }

  delete(data) {
    let url = 'schoolService/studentTable/delete';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  add(data) {
    let url = 'schoolService/studentTable/add';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

}

