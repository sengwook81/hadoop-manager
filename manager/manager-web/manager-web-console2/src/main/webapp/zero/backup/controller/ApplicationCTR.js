/**
 * 
 */
Ext.define('Zero.controller.base.ApplicationCTR',{
	extend: 'Ext.app.Controller',
	stores: [
	         'base.Applications'
	],
	refs: [{
        ref: 'gridApplication',
        selector: 'baseApplication grid'
    }],
	views: ['base.Application'],
	init:function () {
		console.log("Load ApplicationCTR");
		var me = this;
		me.control({
			'#exGrid' : {
				afterrender: function (el) {
					console.log("ExGrid Reder1 ",el,me.getGridApplication().findParentByType('panel'));
					var task = new Ext.util.DelayedTask(function(){
						me.getGridApplication().doLayout();
					});
					task.delay(10);
				}
			},
			'#exGrid button[text=ADD]': {
				click:function (el) {
					var exGrid = el.up('grid');
					console.log(exGrid.store);
					exGrid.store.add({});
					
					me.getGridApplication().doLayout();
				}
			},
			'component': {
				/*
				activate:function () {
					console.log("Component Activate",arguments);
				} ,
				renderer:function() {
					console.log("Component Renderer",arguments);
				},
				afterlayout:function () {
					console.log("Component Afterlayout",arguments[0],arguments[1]);
				} ,
				*/
				deactivate:function (el) {
					//console.log("Component Deactivate",el);
					//me.getGridApplication().doLayout();
					
				}
			}
			,
			'baseApplication ' : {
				refresh:function () {
					console.log("Refresh Grid");
				},
				afterlayout:function () {
					//console.log("BaseApplication Afterlayout",arguments[0],arguments[1]);
				},
				afterrender:function () {
					console.log(me);
					var store = me.getBaseApplicationsStore();
					console.log("Base  Render ",me.getBaseApplicationView());
					store.load(function () {
						
					});
					me.getGridApplication().suspendEvents();
					me.getGridApplication().reconfigure(me.getBaseApplicationsStore());
					me.getGridApplication().resumeEvents();

				}
			},
			'baseApplication button[text=UIREFRESH]': {
				click:function () {
					me.getGridApplication().doLayout();
					//me.getGridApplication().reconfigure(me.getBaseApplicationsStore());
				}
			},
			'baseApplication button[text=REFRESH]': {
				click:function () {
					me.getBaseApplicationsStore().load({});
					console.log("Refresh Store",me.getBaseApplicationsStore());
					//me.getGridApplication().reconfigure(me.getBaseApplicationsStore());
				}
			},
			'baseApplication button[text=ADD]': {
				click:function () {
					me.getBaseApplicationsStore().add({});
				}
			},
			'baseApplication button[text=SAVE]': {
				click:function () {
					me.getBaseApplicationsStore().sync({
						callback:function (){
							//alert("Reload Data");
							//me.getBaseApplicationsStore().load({});
						}
					});
				}
			}
		}
		);
		me.callParent();
	}
});