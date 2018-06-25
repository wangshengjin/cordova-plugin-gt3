#import <Cordova/CDV.h>
@interface NBGeetest : CDVPlugin

- (void)getGeetest:(CDVInvokedUrlCommand*)command;
- (void)initGeetest:(CDVInvokedUrlCommand*)command;
+ (void)callback_gee:(NSDictionary*) backdata;
+ (void)putChallenge:(NSString*) chalg;
@end
