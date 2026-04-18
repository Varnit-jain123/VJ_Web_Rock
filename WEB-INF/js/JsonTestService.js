// VJWebRock Auto-Generated Proxy (JsonTestService)

class JsonTestService {
  add(data) {
    let url = 'schoolService/jsonTest/add';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

  check(data) {
    let url = 'schoolService/jsonTest/checkSession';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  save(data) {
    let url = 'schoolService/jsonTest/save';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

}

