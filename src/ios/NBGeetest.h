#import <Cordova/CDV.h>
@interface NBGeetest : CDVPlugin

- (void)getGeetest:(CDVInvokedUrlCommand*)command;
- (void)initGeetest:(CDVInvokedUrlCommand*)command;
+ (void)callback_gee:(NSDictionary*) backdata;
+ (void)callback_err:(NSDictionary*) errdata;
+ (void)putChallenge:(NSString*) chalg;
@end
