function NBGeetest() {
}

NBGeetest.prototype.getGeetest = function (captchaURL, successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback, "NBGeetest", "getGeetest", [captchaURL]);
};

NBGeetest.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.NBGeetest = new NBGeetest();
  return window.plugins.NBGeetest;
};

cordova.addConstructor(NBGeetest.install);