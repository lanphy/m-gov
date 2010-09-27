//
//  LoadingView.m
//  LoadingView
//
//  Created by Matt Gallagher on 12/04/09.
//  Copyright Matt Gallagher 2009. All rights reserved.
// 
//  Permission is given to use this source code file, free of charge, in any
//  project, commercial or otherwise, entirely at your risk, with the condition
//  that any redistribution (in part or whole) of source code must retain
//  this copyright and permission notice. Attribution in compiled projects is
//  appreciated but not required.
//

#import "LoadingView.h"

//
// NewPathWithRoundRect
//
// Creates a CGPathRect with a round rect of the given radius.
//
CGPathRef NewPathWithRoundRect(CGRect rect, CGFloat cornerRadius) {
	//
	// Create the boundary path
	//
	CGMutablePathRef path = CGPathCreateMutable();
	CGPathMoveToPoint(path, NULL, rect.origin.x, rect.origin.y + rect.size.height - cornerRadius);
	// Top left corner
	CGPathAddArcToPoint(path, NULL, rect.origin.x, rect.origin.y, rect.origin.x + rect.size.width, rect.origin.y, cornerRadius);
	// Top right corner
	CGPathAddArcToPoint(path, NULL, rect.origin.x + rect.size.width, rect.origin.y, rect.origin.x + rect.size.width, rect.origin.y + rect.size.height, cornerRadius);
	// Bottom right corner
	CGPathAddArcToPoint(path, NULL, rect.origin.x + rect.size.width, rect.origin.y + rect.size.height, rect.origin.x, rect.origin.y + rect.size.height, cornerRadius);
	// Bottom left corner
	CGPathAddArcToPoint(path, NULL, rect.origin.x, rect.origin.y + rect.size.height, rect.origin.x, rect.origin.y, cornerRadius);
	// Close the path at the rounded rect
	CGPathCloseSubpath(path);
	return path;
}

@implementation LoadingView

//
// loadingViewInView:
//
// Constructor for this view. Creates and adds a loading view for covering the
// provided aSuperview.
//
// Parameters:
//    aSuperview - the superview that will be covered by the loading view
//
// returns the constructed view, already added as a subview of the aSuperview
//	(and hence retained by the superview)
//
+ (id)loadingViewInView:(UIView *)aSuperview {
	LoadingView *loadingView = [[[LoadingView alloc] initWithFrame:CGRectMake(0, 0, 320, 480)] autorelease];
	if (!loadingView) {
		return nil;
	}
	
	loadingView.opaque = NO;
	loadingView.autoresizingMask =
	UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
	[aSuperview addSubview:loadingView];
	
	const CGFloat DEFAULT_LABEL_WIDTH = 85.0;
	const CGFloat DEFAULT_LABEL_HEIGHT = 50.0;
	CGRect labelFrame = CGRectMake(0, 0, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_HEIGHT);
	 
	UILabel *loadingLabel = [[[UILabel alloc] initWithFrame:labelFrame] autorelease];
	loadingLabel.text = @"正在載入...";
	loadingLabel.textColor = [UIColor whiteColor];
	loadingLabel.backgroundColor = [UIColor clearColor];
	loadingLabel.textAlignment = UITextAlignmentCenter;
	loadingLabel.font = [UIFont boldSystemFontOfSize:[UIFont labelFontSize]];
	loadingLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin;
	
	[loadingView addSubview:loadingLabel];
	UIActivityIndicatorView *activityIndicatorView = [[[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge] autorelease];
	[loadingView addSubview:activityIndicatorView];
	activityIndicatorView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin;
	[activityIndicatorView startAnimating];
	
	//CGFloat totalHeight = loadingLabel.frame.size.height + activityIndicatorView.frame.size.height;
	//labelFrame.origin.x = floor(0.5 * (loadingView.frame.size.width - DEFAULT_LABEL_WIDTH));
	//labelFrame.origin.y = floor(0.5 * (loadingView.frame.size.height - totalHeight)) -7;
	labelFrame.origin.x = loadingView.frame.size.width/2 - DEFAULT_LABEL_WIDTH/2 + (activityIndicatorView.frame.size.width)/2;
	labelFrame.origin.y = loadingView.frame.size.height/2 - DEFAULT_LABEL_HEIGHT/2;
	loadingLabel.frame = labelFrame;
	
	CGRect activityIndicatorRect = activityIndicatorView.frame;
	// Set the activity origin
	activityIndicatorRect.origin.x = 0.5 * (loadingView.frame.size.width - activityIndicatorRect.size.width);
	//activityIndicatorRect.origin.y = loadingLabel.frame.origin.y + loadingLabel.frame.size.height -4;
	activityIndicatorRect.origin.x = loadingView.frame.size.width/2 - activityIndicatorRect.size.width - DEFAULT_LABEL_WIDTH/2;
	activityIndicatorRect.origin.y = loadingView.frame.size.height/2 - activityIndicatorRect.size.height/2;
	activityIndicatorView.frame = activityIndicatorRect;
	
	// Set up the fade-in animation
	CATransition *animation = [CATransition animation];
	[animation setType:kCATransitionFade];
	[[aSuperview layer] addAnimation:animation forKey:@"layerAnimation"];
	
	return loadingView;
}

//
// removeView
//
// Animates the view out from the superview. As the view is removed from the
// superview, it will be released.
//
- (void)removeView {
	UIView *aSuperview = [self superview];
	[super removeFromSuperview];
	
	// Set up the animation
	CATransition *animation = [CATransition animation];
	[animation setType:kCATransitionFade];
	
	[[aSuperview layer] addAnimation:animation forKey:@"layerAnimation"];
}

//
// drawRect:
//
// Draw the view.
//

- (void)drawRect:(CGRect)rect {
	//rect = CGRectInset(rect, 0, 0);
	rect = CGRectInset(rect, 40, 200);
	CGPathRef roundRectPath = NewPathWithRoundRect(rect, 10.0);
	
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	const CGFloat BACKGROUND_OPACITY = 0.75;
	CGContextSetRGBFillColor(context, 0, 0, 0, BACKGROUND_OPACITY);
	CGContextAddPath(context, roundRectPath);
	CGContextFillPath(context);
	
	const CGFloat STROKE_OPACITY = 0.4;
	CGContextSetRGBStrokeColor(context, 0.5, 0.5, 0.5, STROKE_OPACITY);
	CGContextAddPath(context, roundRectPath);
	CGContextStrokePath(context);
	
	CGPathRelease(roundRectPath);
}

//
// dealloc
//
// Release instance memory.
//

- (void)dealloc {
    [super dealloc];
}

@end
