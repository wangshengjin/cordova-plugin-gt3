#import "NBGeetest.h"
#import <Cordova/CDV.h>
#import <GT3Captcha/GT3Captcha.h>

@implementation NBGeetest
static NSString* challenge;
static NBGeetest* nbSelf;
GT3CaptchaManager* manager;
CDVInvokedUrlCommand* currentCommand;
- (void)pluginInitialize {
    
}

+ (void)callback_gee:(NSDictionary*) backdata{
    [backdata setValue:challenge forKey:@"challenge"];
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:backdata options:NSJSONWritingPrettyPrinted error:&error];
    NSString *jsonString;
    if (!jsonData) {
        NSLog(@"%@",error);
    }else{
        jsonString = [[NSString alloc]initWithData:jsonData encoding:NSUTF8StringEncoding];
    }
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:jsonString];
    [nbSelf.commandDelegate sendPluginResult:pluginResult callbackId:currentCommand.callbackId];
}

+(void)putChallenge:(NSString*) chalg{
    challenge = chalg;
}

- (void)getGeetest:(CDVInvokedUrlCommand*)command {
    [manager startGTCaptchaWithAnimated:YES];
    nbSelf = self;
    currentCommand = command;
}


- (void)initGeetest:(CDVInvokedUrlCommand*)command {
    NSString* api_1 = command.arguments[0];
    NSString* api_2 = @"";
    manager = [[GT3CaptchaManager alloc] initWithAPI1:api_1 API2:api_2 timeout:5.0];
    manager.delegate = self.appDelegate;
    manager.viewDelegate = self.appDelegate;
    [manager useVisualViewWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleDark]];
    [manager registerCaptcha:nil];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:TRUE];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
