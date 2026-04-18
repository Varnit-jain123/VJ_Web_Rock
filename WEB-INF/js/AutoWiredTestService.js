// VJWebRock Auto-Generated Proxy (AutoWiredTestService)

class AutoWiredTestService {
  setup(data) {
    let url = 'schoolService/autowired/setup';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

  test(data) {
    let url = 'schoolService/autowired/test';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

