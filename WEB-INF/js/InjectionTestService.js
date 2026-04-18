// VJWebRock Auto-Generated Proxy (InjectionTestService)

class InjectionTestService {
  print(data) {
    let url = 'schoolService/injection/print';
    if (data) {
      let params = new URLSearchParams(data).toString();
      if (params) url += '?' + params;
    }
    return fetch(url, {
      method: 'GET'
    }).then(r => r.text());
  }

}

