/**
 * 
 */
Ext.define('Zero.controller.app.AppConfigCTR',{
	extend: 'Ext.app.Controller',
	refs: [{
        ref: 'gridAppConfig',
        selector: '#gridAppConfig'
    },
//	{
//    	ref: 'appAppConfig',
//        selector: 'appAppConfig'
//	}
	],
	init:function () {
		
		var me = this;
	
		//console.log("Load ConfigPropertiesCTR",	me.getAppAppConfig().param);
		me.control({
			'appAppConfig':{
				afterrender:function () {
//					alert("AppConfig Render");
//					console.log("After Render AppConfig View",me,me.getGridAppConfig(),me.getGridAppConfig().getStore());
//					me.getGridAppConfig().getStore().load({
//						params:me.getAppAppConfig().param,
//						callback:function () {
//							console.log("Callback Call ",arguments);
//						}
//					});
					
				}
			},
			'#gridAppConfig button[text=REFRESH]' : {
				click:function (el) {
					var targetGrid = el.up('grid');
					var targetView = targetGrid.up('panel');
					targetGrid.getStore().load({
						params:targetView.param,
						callback:function () {
							console.log("Callback Call ",arguments);
						}
					});
				}
			},
			'#gridAppConfig button[text=ADD]':{
				click:function (el) {
					console.log("Click Add Row");
					var targetGrid = el.up('grid');
					var targetView = targetGrid.up('panel');
					var configView = Ext.create('Zero.view.app.AppConfigPop',targetView.param);
					Ext.create("Ext.window.Window",{
                        title : 'Title',
                        width : 800,
                        closable : true,
                        height: 500,
                        layout:'fit',
                        ownerCt : targetGrid.up('panel'),
                        modal : true,
                        items: configView,
                        buttons:[
                             {
                                 text:'Finish',
                                 handler:function () {
                                     this.up('window').close();
                                 }
                             }
                        ],
                        listeners:{
                             close:function () {
                            	 console.log("ConfigView Close",configView.gridStore);
                            	 var isUpdated = false;
                            	 configView.gridStore.each(function (record) {
                            		if(record.get('chk')) {
                            			console.log(record);
                            			targetGrid.getStore().add(record.data);
                            			isUpdated = true;
                            		} 
                            	 });
                            	 if(isUpdated) {
                            		 targetGrid.getStore().sync(
                 					{
                 						callback:function (){
                							//console.log("me.getBaseConfigProperties().param",me.getGridAppConfig().param);
                 							targetGrid.getStore().load({
                								params:targetView.param,
                								callback:function () {
                									console.log("Callback Call ",arguments);
                								}
                							});
                 					}});
                            	 }
                             },
                             scope:this
                         }
                	}
                    ).show();
				}
			},
			'#gridAppConfig button[text=SAVE]':{
				click:function (el) {
					console.log("Click Save Row" , arguments);
					var targetGrid = el.up('grid');
					targetGrid.getStore().sync({
						callback:function (){
							targetGrid.getStore().load({
								params:me.getAppAppConfig().param,
								callback:function () {
									console.log("Callback Call ",arguments);
								}
							});
						},
						failure:function() {
							console.log("Sync Fail" , arguments);
							alert("Fail");
						}
					},me);
					
				}
			}
		});
		me.callParent();
	}
});