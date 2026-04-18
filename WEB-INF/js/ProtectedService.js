// VJWebRock Auto-Generated Proxy (ProtectedService)

class ProtectedService {
  getData(data) {
    let url = 'schoolService/secure/data';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

