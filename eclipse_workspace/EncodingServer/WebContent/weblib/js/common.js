var commonFn = {
		logout: function(logoutUrl) {
			$.post(logoutUrl, null, function() {
				location.reload();
			})
		},
		connectNotice: {
			ws: null,
			start: function(url, key) {
				this.ws = new WebSocket(url+'/'+key);
				this.ws.onopen = function(evt) {
					//console.log(evt);
					//toastr["success"]('공지서버 연결 성공');
				};
				this.ws.onmessage = function(evt) {
					//console.log(evt);
					var token = '%%';
					var status = evt.data.split(token+token)[0].split(token)[2];
					var msg = evt.data.split(token+token)[1];
					toastr.options.timeOut = 100000;
					toastr.options.extendedTimeOut = 10000;
					toastr["info"](msg);
					toastr.options.timeOut = 3000;
					toastr.options.extendedTimeOut = 1000;
				};
				this.ws.onerror = function(evt) {
					//console.log(evt);
					//toastr["warning"]("에러");
				}
				this.ws.onclose = function(evt) {
					//console.log(evt);
					//toastr["warning"]("연결 끊김");
				}
			},
			close: function() {
				this.ws.close();
			}
		}
}

var e = {
		g: {
			g1: function() {
				FnObj.mainTextAnim.textArr=['Praise The Sun!!', 'a Knight of Astora.', '... nope... YOU DIED.', '\\[T]/'];
				$('body').css('background-image', 'url(\'/weblib/imgs/mainPage/darksouls.jpg\')');
			}
		}
}