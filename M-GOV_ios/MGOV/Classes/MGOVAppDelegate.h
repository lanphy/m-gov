//
//  MGOVAppDelegate.h
//  MGOV
//
//  Created by Shou 2010/8/24.
//  Copyright NTU Mobile HCI Lab 2010. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HybridViewController.h"
#import "MyCaseViewController.h"
#import "QueryViewController.h"
#import "PrefViewController.h"
#import "PrefAccess.h"
#import "CaseAddViewController.h"

@interface MGOVAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
	UITabBarController *tabBarController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) UITabBarController *tabBarController;

@end

