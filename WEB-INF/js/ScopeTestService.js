// VJWebRock Auto-Generated Proxy (ScopeTestService)

class ScopeTestService {
  get(data) {
    let url = 'schoolService/scope/get';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  set(data) {
    let url = 'schoolService/scope/set';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

