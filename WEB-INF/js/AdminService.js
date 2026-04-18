// VJWebRock Auto-Generated Proxy (AdminService)

class AdminService {
  list(data) {
    let url = 'schoolService/admin/list';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  update(data) {
    let url = 'schoolService/admin/update';
    return fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
    }).then(r => r.text());
  }

}

