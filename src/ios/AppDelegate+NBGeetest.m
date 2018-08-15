//
//  AppDelegate+NBGeetest.m
//  delegateExtention
//
//  Created by wangshengjin on 18/8/15.
//  Copyright (c) 2018年 NBGeetest. All rights reserved.
//

#import "AppDelegate+NBGeetest.h"
#import <GT3Captcha/GT3Captcha.h>
#import "NBGeetest.h"

@implementation AppDelegate (NBGeetest)

#pragma mark GT3CaptchaManagerDelegate
- (void)gtCaptcha:(GT3CaptchaManager *)manager errorHandler:(GT3Error *)error {
    NSDictionary *dict;
    //处理验证中返回的错误
    if (error.code == -999) {
        // 请求被意外中断, 一般由用户进行取消操作导致, 可忽略错误
        dict = @{@"code":@"-999", @"msg":@"请求被意外中断"};
    }
    else if (error.code == -10) {
        // 预判断时被封禁, 不会再进行图形验证
        dict = @{@"code":@"-10", @"msg":@"预判断时被封禁"};
    }
    else if (error.code == -20) {
        // 尝试过多
        dict = @{@"code":@"-20", @"msg":@"尝试过多"};
    }
    else {
        // 网络问题或解析失败, 更多错误码参考开发文档
        dict = @{@"code":@"-2001", @"msg":@"网络问题或解析失败"};
    }
    [NBGeetest callback_err:dict];
}

- (void)gtCaptchaUserDidCloseGTView:(GT3CaptchaManager *)manager {
    NSDictionary *dict = @{@"code":@"-1001", @"msg":@"用户关闭验证"};
    [NBGeetest callback_err:dict];
    NSLog(@"User Did Close GTView.");
}


/** 修改API2的请求
 - (void)gtCaptcha:(GT3CaptchaManager *)manager willSendSecondaryCaptchaRequest:(NSURLRequest *)originalRequest withReplacedRequest:(void (^)(NSMutableURLRequest *))replacedRequest {
 
 }
 */

// ** 不使用默认的二次验证接口
- (void)gtCaptcha:(GT3CaptchaManager *)manager didReceiveCaptchaCode:(NSString *)code result:(NSDictionary *)result message:(NSString *)message {
    
    __block NSMutableString *postResult = [[NSMutableString alloc] init];
    [result enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL * stop) {
        [postResult appendFormat:@"%@=%@&",key,obj];
    }];
    [NBGeetest callback_gee:result];
    [manager closeGTViewIfIsOpen];
    
}

- (BOOL)shouldUseDefaultSecondaryValidate:(GT3CaptchaManager *)manager {
    return NO;
}

// ** 自定义处理API1返回的数据并将验证初始化数据解析给管理器
- (NSDictionary *)gtCaptcha:(GT3CaptchaManager *)manager didReceiveDataFromAPI1:(NSDictionary *)dictionary withError:(GT3Error *)error {
    NSString* sst = dictionary[@"challenge"];
    [NBGeetest putChallenge:sst];
    NSDictionary * aabb;
    return aabb;
}

/** 修改API1的请求 */
- (void)gtCaptcha:(GT3CaptchaManager *)manager willSendRequestAPI1:(NSURLRequest *)originalRequest withReplacedHandler:(void (^)(NSURLRequest *))replacedHandler {
    NSMutableURLRequest *mRequest = [originalRequest mutableCopy];
    NSString *newURL = [NSString stringWithFormat:@"%@?t=%.0f", originalRequest.URL.absoluteString, [[[NSDate alloc] init]timeIntervalSince1970]];
    mRequest.URL = [NSURL URLWithString:newURL];
    
    replacedHandler(mRequest);
}

@end

