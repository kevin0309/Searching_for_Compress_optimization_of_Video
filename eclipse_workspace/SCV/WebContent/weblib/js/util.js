var $$ = {
	copyToClipboard: function(val) {
		var t = document.createElement("textarea");
		document.body.appendChild(t);
		t.value = val;
		t.select();
		document.execCommand('copy');
		document.body.removeChild(t);
		toastr["info"]('클립보드에 복사되었습니다.');
	},
	/**
	 * 브라우저 환경에 따라 함수를 실행시킨다.<br>
	 * 실핼시킬 함수는 funcObj에 지정한다.
	 * 
	 * @param funcObj	: (object) 실행시킬 함수를 담는 객체<br><br>
	 * <b>funcObj 필드는 다음과 같다.</b><br>
	 * @param pc		: (function) PC 브라우저일때 동작 (PC의 최상위 메소드이므로 해당 파라미터 설정시 하위함수가 동작하지 않음)
	 * @param oldIE	: (function) PC 브라우저 - IE 10 이하 버전에서 동작
	 * @param IE11		: (function) PC 브라우저 - IE 11 에서 동작
	 * @param edge		: (function) PC 브라우저 - IE 12 이상 버전, Edge 브라우저에서 동작
	 * @param chrome	: (function) PC 브라우저 - Chrome 에서 동작
	 * @param firefox	: (function) PC 브라우저 - Firefox 에서 동작
	 * @param opera	: (function) PC 브라우저 - Opera 에서 동작
	 * @param pcEtc	: (function) PC 브라우저 - 위의 PC브라우저에 해당되지 않는 브라우저일 때 동작
	 * <br><br>
	 * @param mobile	: (function) 모바일 브라우저일때 동작 (모바일의 최상위 메소드이므로 해당 파라미터 설정시 하위함수가 동작하지 않음)
	 * @param android	: (function) 모바일 브라우저 - Android 디바이스 에서 동작
	 * @param ios	: (function) 모바일 브라우저 - IOS 디바이스 에서 동작
	 * @param mobileEtc: (function) 모바일 브라우저 - 위의 모바일 디바이스에 해당되지 않는 디바이스일 때 동작
	 * <br><br>
	 * @param error	: (function) javascript에서 window.navigator.platform 객체를 지원하지 않는 브라우저의 경우 동작 
	 */
	browserFilter: function(funcObj) {
		var browsers = 'win16|win32|win64|mac|macintel';
		
		if (navigator.platform) {
			if (browsers.indexOf(navigator.platform.toLowerCase()) < 0) {
				//mobile browsers
				if (typeof(funcObj.mobile) === 'function') {
					funcObj.mobile();
					return;
				}
				
				var userAgent = navigator.userAgent.toLowerCase();
				
				if (userAgent.match('android') != null) {
					//Android devices
					if (typeof(funcObj.android) === 'function') {
						funcObj.android();
						return;
					}
				}
				else if (userAgent.indexOf('iphone')>-1 || userAgent.indexOf('ipad')>-1 || userAgent.indexOf('ipod')>-1) {
					//Apple devices
					if (typeof(funcObj.ios) === 'function') {
						funcObj.ios();
						return;
					}
				}
				
				//other mobile devices
				if (typeof(funcObj.mobileEtc) === 'function') {
					funcObj.mobileEtc();
				}
				else {
					console.warn('browserFilter - Undefined Mobile client (recommendation : set "mobileEtc" function.)\n\nCheck out the client info below.\nPlatform : Mobile\nClient userAgent : '+userAgent);
				}
			}
			else {
				//PC browsers
				if (typeof(funcObj.pc) === 'function') {
					funcObj.pc();
					return;
				}
				
				var userAgent = navigator.userAgent;
				
				if (userAgent.indexOf('MSIE ') > 0) {
					//IE 10 or old IE browsers
					if (typeof(funcObj.oldIE) === 'function') {
						funcObj.oldIE();
						return;
					}
				}
				else if (userAgent.indexOf('Trident/') > 0) {
					//IE 11
					if (typeof(funcObj.IE11) === 'function') {
						funcObj.IE11();
						return;
					}
				}
				else if (userAgent.indexOf('Edge/') > 0) {
					//Edge (IE 12+)
					if (typeof(funcObj.edge) === 'function') {
						funcObj.edge();
						return;
					}
				}
				else if (userAgent.toLowerCase().indexOf('chrome') > 0 && navigator.vendor.toLowerCase().indexOf("google") > -1) {
					//Chrome
					if (typeof(funcObj.chrome) === 'function') {
						funcObj.chrome();
						return;
					}
				}
				else if (userAgent.toLowerCase().indexOf('firefox') > -1) {
					//Firefox
					if (typeof(funcObj.firefox) === 'function') {
						funcObj.firefox();
						return;
					}
				}
				else if (userAgent.indexOf('OPR')) {
					//Opera
					if (typeof(funcObj.opera) === 'function') {
						funcObj.opera();
						return;
					}
				}

				//other PC browsers
				if (typeof(funcObj.pcEtc) === 'function') {
					funcObj.pcEtc();
				}
				else {
					console.warn('browserFilter - Undefined PC client. (recommendation : set "pcEtc" function.)\n\nCheck out the client info below.\nPlatform : PC\nClient userAgent : '+userAgent);
				}
			}
		}
		else {
			//지원하지 않는 자바스크립트버전
			console.error('browserFilter - This browser does not support the JavaScript required for this function(browserFilter).')
			if (typeof(funcObj.error) === 'function')
				funcObj.error();
			else {
				console.warn('browserFilter - Error callback function undefined. (recommendation : set "error" function.)');
			}
		}
	},
	getURLParam: function() {
		var param = {};
		var url = location.href.split('#')[0];
		var str = url.split('?');
		var str2 = str[1].split('&');
		
		for (var i = 0; i < str2.length ;i++) {
			var str3 = str2[i].split('=');
			param[str3[0]] = str3[1];
		}
		if (param == '')
			console.log('VALUE NOT FOUND');
		else
			return param;
	},
	/**
	 * 값을받아 폼을 생성한뒤 제출하는 메소드
	 * get, post 메소드를 사용하며
	 * @param url : (String) form의 action속성에 들어갈 URL
	 * @param param : (Object) 전속할 파라미터 객체
	 * @param (target) : (target) form의 target속성
	 * @param (submit) : (boolean) 폼 생성 후 자동으로 submit 시킬 것인지(기본 true)
	 */
	sendParam: {
			targetURL: '',
			reqParam: {},
			target: '',
			submit: true,
			formID: 'sendParamDefaultFormID',
			get: function(url, param, target, submit) {
				this.targetURL = url;
				this.reqParam = param;
				this.target = target;
				this.submit = submit;
				if (typeof(submit) == 'undefined')
					this.submit = true;
				
				if (!sendParam.checkArguments())
					return;
				else {
					this.addForm('get');
					for (prop in this.reqParam) {
						this.addInput(prop, this.reqParam[prop]);
					}
					if (this.submit)
						this.submitForm();
				}
			},
			post: function(url, param, target, submit) {
				this.targetURL = url;
				this.reqParam = param;
				this.target = target;
				this.submit = submit;
				if (typeof(submit) == 'undefined')
					this.submit = true;
				
				if (!sendParam.checkArguments())
					return;
				else {
					this.addForm('post');
					for (prop in this.reqParam) {
						this.addInput(prop, this.reqParam[prop]);
					}
					if (this.submit)
						this.submitForm();
				}
			},
			checkArguments: function() {
				if (this.targetURL == '' || this.targetURL == null) {
					console.log("Need URL argument");
					return false;
				}
				if (this.submit == '' || this.submit == null) {
					return true;
				}
				else if (typeof(this.submit) != 'boolean') {
					console.log('Put boolean type to submit argument');
					return false;;
				}
				return true;
			},
			addForm: function(method) {
				var $form = $('<form></form>');
				$form.attr('action', this.targetURL);
				$form.attr('method', method);
				$form.attr('id', this.formID);
				if (!(this.target == '' || this.target == null))
					$form.attr('target', this.target);
				$form.appendTo('body');
			},
			addInput: function(name, value) {
				var $input = $('<input>');
				$input.attr('type', 'hidden');
				$input.attr('name', name);
				$input.attr('value', value);
				$input.appendTo('#'+this.formID);
			},
			submitForm: function() {
				if ($('#'+this.formID).length == 0) {
					alert('Form not found.');
				}
				else {
					$('#'+this.formID).submit();
					$('#'+this.formID).remove();
				}
			}
	},
	mask: {
		open: function(cssOpt) {
		$('#defaultMask').remove();
		$('body').append(
			'<div class="mask" id="defaultMask"></div>'
		);
		if (cssOpt) {
			setTimeout(function() {
				var css = Object.keys(cssOpt);
				for (var i = 0; i < css.length; i++)
					$('#defaultMask').css(css[i], cssOpt[css[i]]);
			}, 100);
		}
	},
	close: function(maskId, closeCallbackFn) {
		$('#defaultMask').css('opacity', '0');
		setTimeout(function() {
			$('#defaultMask').remove();
			if (typeof(closeCallbackFn) === 'function')
				closeCallbackFn();
		}, 500);
	}
	},
	loadingMask: {
		open: function(maskId) {
			$('#mask'+maskId).remove();
			$('body').append(
				'<div class="mask" id="mask'+maskId+'">' +
					'<div>' +
						'<div class="loading" id="loading'+maskId+'"></div>' +
						'<h1></h1>' +
					'</div>' +
				'</div>'
			);
		},
		close: function(maskId, closeCallbackFn) {
			$('#mask'+maskId).css('opacity', '0');
			setTimeout(function() {
				$('#mask'+maskId).remove();
				if (typeof(closeCallbackFn) === 'function')
					closeCallbackFn();
			}, 500);
		}
	},
	popupLayer: {
		open: function(layerId) {
			var _this = this;
			var $htmlToAdd = $('<div class="popupLayer" id="popup'+layerId+'">'+
									'<div class="popupLayerWrapper">' +
										'<div id="popupCloseBtn" style="position: fixed; z-index: 1; right: 15px; float:right; font-size: 30px;">X</div>' +
										'<div class="popupLayerContent"></div>' +
									'</div>' +
								'</div>');
			$htmlToAdd.find('#popupCloseBtn').click(function() {_this.close(layerId);});
			$('body').append($htmlToAdd);
			
			setTimeout(function() {
				$('#popup'+layerId).css('opacity', '1');
			}, 100);
			
			return $htmlToAdd;
		},
		close: function(layerId) {
			$('#popup'+layerId).css('opacity', '0');
			setTimeout(function() {
				$('#popup'+layerId).remove();
			}, 500);
		}
	},
	toast: {
		push: function(msg) {
			var obj = {
				pc: function() {
					$$.toast.pc.push(msg);
				},
				mobile: function() {
					$$.toast.mobile.push(msg);
				}
			}
			$$.browserFilter(obj);
		},
		mobile: {
			/**
			 * 모바일용 toast 메세지를 띄운다.<br>
			 * 하단에 작게 반투명한 회색 박스로 만들어진다.<br>
			 * 텍스트가 두줄을 넘어가면 잘리므로 주의
			 * 
			 * @param msg (String) toast 메시지 
			 */
			push: function(msg) {
				$('.toastMsg_mobile').remove();
				var curTime = new Date();
				curTime = curTime.getTime();
				var alertId = 'alert'+curTime;
				$('body').append(
					'<div class="toastMsg_mobile" id="'+alertId+'">' +
						'<div>' + msg + '</div>' +
					'</div>'
				)
				setTimeout (function() {
					$('#'+alertId).css('transition', 'opacity 0.4s ease 0s');
					$('#'+alertId).css('opacity', '1');
					setTimeout(function() {
						$('#'+alertId).css('transition', 'opacity 1s ease-in 3s')
						$('#'+alertId).css('opacity', '0');
						setTimeout(function() {
							//$('#'+alertId).remove();
						}, 4200);
					}, 600);
				}, 100);
			}
		},
		pc: {
			push: function(msg) {
				$('.toastMsg').remove();
				var curTime = new Date();
				curTime = curTime.getTime();
				var alertId = 'alert'+curTime;
				$('body').append(
					'<div class="toastMsg" id="'+alertId+'">' +
						'<div>' + msg + '</div>' +
					'</div>'
				)
				setTimeout (function() {
					$('#'+alertId).css('transition', 'opacity 0.4s ease 0s');
					$('#'+alertId).css('opacity', '1');
					setTimeout(function() {
						$('#'+alertId).css('transition', 'opacity 1s ease-in 3s')
						$('#'+alertId).css('opacity', '0');
						setTimeout(function() {
							//$('#'+alertId).remove();
						}, 4200);
					}, 600);
				}, 100);
			}
		}
	}
}

/**
 * 특정 기능을 톡립적으로 사용하기 위한 함수객체.<br>
 * 필요한 초기 설정값을 지정해 줄 수 있으며 기본적인 기능들을 포함한다.<br>
 * <br>
 * 사용할 수 있는 메소드는 다음과 같다.<br>
 * setAjaxTimeout : options.ajaxTimeout 을 설정하는 setter 메소드<br>
 * setMaskId : options.componentId 를 설정하는 setter 메소드<br>
 * chkStatus : 현재 객체의 status 상태를 체크하여 ready, ajaxTimeout, ajaxError 일 경우 true를 리턴<br>
 * doGet : HTTP GET 메소드를 사용하여 ajax요청<br>
 * doPost : HTTP POST 메소드를 사용하여 ajax요청<br>
 * openMask : 로딩 이펙트가 포함된 마스크를 띄움<br>
 * closeMask : 마스크를 닫음<br>
 * alert : 모바일용 alert창을 띄움
 * 
 * @param options 초기 설정값, 객체 내에서 쓰일 변수들을 담는 객체<br><br>
 * 		<b>하위 메소드 실행시 필요한 options의 필드는 다음과 같다.</b>
 * @param componentId (String, default: '(현재시간정보)')
 * 	openMask, closeMask를 사용할 때 필요한 설정값
 * @param ajaxTimeout (int, default: 1000 * 10 (10초))
 * 	doPost, doGet 사용할 때 ajax 요청시 시간 만료까지 대기하는 시간(단위 ms)
 */
function $$Component(options) {
	if (typeof(options) === 'object')
		this.options = options;
	else
		this.options = {};
	
	//add default options
	if (typeof(this.options.ajaxTimeout) === 'undefined') {
		this.options.ajaxTimeout = 1000 * 10; //ajax 요청 대기시간 10초
	}
	if (typeof(this.options.componentId) === 'undefined') {
		var curTime = new Date();
		curTime = curTime.getTime();
		this.options.componentId = ''+curTime;
	}
	
	this.iconUrl = {
		noImage: '/resources/img/msadmin/main/no_image.png',
		logo: '/resources/img/msadmin/main/logo.png',
		heart: 'heart.png',
		balloon: 'balloon.png',
		eye: 'eye.png',
		clip: 'clip.png'
	}
	this.maskStatus = false;
}

/**
 * 현재 상태에 대한 정보를 담는 객체를 만들기위한 함수객체<br>
 * <br>
 * 사용중인 코드는 다음과 같다.<br>
 * initiating : 시작 프로세스 진행중 <br>
 * ready : 준비<br>
 * ajaxTimeout : doGet, doPost 사용중 ajax 시간만료<br>
 * ajaxError : doGet, doPost 사용중 ajax 에러<br>
 * unauthorized : 권한 부족
 * 
 * @param statusCode (String, default: 'ready') 상태코드
 * @param statusMsg (String, default: '준비') 상태메시지
 */
function Status(statusCode, statusMsg) {
	if (typeof(statusCode) === 'undefined' && typeof(statusMsg) === 'undefined') {
		this.statusCode = 'ready';
		this.statusMsg = '준비';
	}
	else {
		if (typeof(statusCode) === 'undefined')
			this.statusCode = '';
		else
			this.statusCode = statusCode;
		
		if (typeof(statusMsg) === 'undefined')
			this.statusMsg = '';
		else
			this.statusMsg = statusMsg;
	}
}
Status.prototype.toString = function() {
	return 'Status - '+this.statusCode+' : '+this.statusMsg;
}
/**
 * 콘솔창에 에러로그를 띄움
 */
Status.prototype.errorLog = function() {
	console.error(this.toString());
}
/**
 * 콘솔창에 로그를 띄움
 */
Status.prototype.log = function() {
	console.log(this.toString());
}
$$Component.prototype.status = new Status();

/**
 * options.ajaxTimeout 을 설정하는 setter 메소드
 * @param millisecond
 */
$$Component.prototype.setAjaxTimeout = function(millisecond) {
	if (typeof(millisecond) === 'number')
		this.options.ajaxTimeout = millisecond;
}
/**
 * options.componentId 를 설정하는 setter 메소드
 * @param componentId
 */
$$Component.prototype.setMenuId = function(componentId) {
	if (typeof(componentId) === 'String')
		this.options.componentId = componentId;
}
/**
 * 현재 객체의 status 상태를 체크하여 ready, ajaxTimeout, ajaxError 일 경우 true를 리턴한다.
 * @returns {Boolean}
 */
$$Component.prototype.chkStatus = function() {
	if (this.status.statusCode == 'ready' || this.status.statusCode == 'ajaxTimeout' || this.status.statusCode == 'ajaxError')
		return true;
	else {
		this.status.errorLog();
		this.alert('페이지 로딩 오류발생.<br>새로고침 후 다시 시도해주세요.');
		return false;
	}
}

/**
 * HTTP GET 메소드를 사용하여 ajax요청을 한다.
 * 
 * @param url (String) ajax 요청을 할 url주소
 * @param dataParamObj (Object) ajax요청 시 전달할 데이터를 담은 객체
 * @param success (function) ajax요청이 성공했을 때 콜백함수
 * @param error (function) ajax요청이 실패했을 때 콜백함수
 */
$$Component.prototype.doGet = function(url, dataParamObj, success, error) {
	var _this = this;
	$$.ajax.get(url, dataParamObj, success, function(jqXHR, textStatus) {
		if (textStatus === 'timeout') {
			_this.status = new Status('ajaxTimeout', 'ajax 연결시간이 초과되었습니다. 다시한번 시도해주세요.');
			_this.status.errorLog();
			_this.alert('서버연결이 지연되고있습니다.<br>잠시 후에 다시 시도해주세요.');
		}
		else {
			_this.status = new Status('ajaxError', 'ajax 에러 발생! ('+ jqXHR.status + ' ' + jqXHR.statusText + ')');
			_this.status.errorLog();
			_this.alert('서버오류 발생.<br>잠시 후에 다시 시도해주세요.');
		}
		
		error(jqXHR, textStatus);
	}, this.options.ajaxTimeout);
}

/**
 * HTTP POST 메소드를 사용하여 ajax요청(dataType: JSON)을 한다.
 * 
 * @param url (String) ajax 요청을 할 url주소
 * @param dataParamObj (Object) ajax요청 시 전달할 데이터를 담은 객체
 * @param success (function) ajax요청이 성공했을 때 콜백함수
 * @param error (function) ajax요청이 실패했을 때 콜백함수
 */
$$Component.prototype.doPost = function(url, dataParamObj, success, error) {
	var _this = this;
	$$.ajax.post(url, dataParamObj, success, function(jqXHR, textStatus) {
		if (textStatus === 'timeout') {
			_this.status = new Status('ajaxTimeout', 'ajax 연결시간이 초과되었습니다. 다시한번 시도해주세요.');
			_this.status.errorLog();
			_this.alert('서버연결이 지연되고있습니다.<br>잠시 후에 다시 시도해주세요.');
		}
		else {
			_this.status = new Status('ajaxError', 'ajax 에러 발생! ('+ jqXHR.status + ' ' + jqXHR.statusText + ')');
			_this.status.errorLog();
			_this.alert('서버오류 발생.<br>잠시 후에 다시 시도해주세요.');
		}
		
		error(jqXHR, textStatus);
	}, this.options.ajaxTimeout);
}

/**
 * 로딩 이펙트가 포함된 마스크를 띄운다.<br>
 * body 에 추가되며 마스크의 id는 options.componentId를 참조한다.
 */
$$Component.prototype.openMask = function() {
	if (this.maskStatus === false) {
		$$.loadingMask.open(this.options.componentId);
		this.maskStatus = true;
	}
}

/**
 * 마스크를 닫는다.
 */
$$Component.prototype.closeMask = function() {
	if (this.maskStatus === true) {
		var _this = this;
		$$.loadingMask.close(this.options.componentId, function() {_this.maskStatus = false;});
	}
}

$$Component.prototype.openPopupLayer = function() {
	return $$.popupLayer.open(this.options.componentId);
}

$$Component.prototype.closePopupLayer = function() {
	$$.popupLayer.close(this.options.componentId);
}

$$Component.prototype.toast = function(msg) {
	$$.toast.mobile.push(msg);
}