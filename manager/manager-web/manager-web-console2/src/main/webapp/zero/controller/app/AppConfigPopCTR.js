/**
 * 
 */
Ext.define('Zero.controller.app.AppConfigPopCTR',{
	extend: 'Ext.app.Controller',
	refs: [{
        ref: 'gridAppConfigSrc',
        selector: '#gridAppConfigSrc'
    },{
    	ref: 'appAppConfigPop',
        selector: 'appAppConfigPop'
    }],
	init:function () {
		console.log("Load ConfigPropertiesCTR",arguments);
		var me = this;
		me.control({
			'appAppConfigPop':{
				afterrender:function () {
					console.log("After Render AppConfig View");//,me,me.getGridAppConfig(),me.getGridAppConfig().getStore());
					me.getGridAppConfigSrc().getStore().load({
						params:me.getAppAppConfigPop().param,
						callback:function () {
							console.log("Callback Call ",arguments);
						}
					});
				}
			}
		});
		me.callParent();
	}
});