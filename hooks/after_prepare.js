module.exports = function(context) {

  var path              = context.requireCordovaModule('path'),
      fs                = context.requireCordovaModule('fs'),
      Q                 = context.requireCordovaModule('q'),
      cordova_util      = context.requireCordovaModule('cordova-lib/src/cordova/util'),
      platforms         = context.requireCordovaModule('cordova-lib/src/platforms/platforms')

  var deferral = new Q.defer();
  var projectRoot = cordova_util.cdProjectRoot();

  context.opts.platforms.filter(function(platform) {
      var pluginInfo = context.opts.plugin.pluginInfo;
      return pluginInfo.getPlatformsArray().indexOf(platform) > -1;
      
  }).forEach(function(platform) {
      var platformPath = path.join(projectRoot, 'platforms', platform);
      var platformApi = platforms.getPlatformApi(platform, platformPath);
      var platformInfo = platformApi.getPlatformInfo();
      var wwwDir = platformInfo.locations.www;

      if (platform == 'ios') {
        var xmlHelpers = context.requireCordovaModule('cordova-common').xmlHelpers;
        var pluginXml = path.join(projectRoot, 'config.xml');
        var doc = xmlHelpers.parseElementtreeSync(pluginXml);
        var confiles = doc.findall('name');
        var projename = confiles[0].text;
        var inputpath = path.join(projectRoot, 'plugins/cordova-plugin-gt3/src/ios/AppDelegate.m');
        var outpath = path.join(platformPath, projename, 'Classes/AppDelegate.m');
        fs.writeFileSync(outpath, fs.readFileSync(inputpath));
      } 
  });

  deferral.resolve();
  return deferral.promise;
}
