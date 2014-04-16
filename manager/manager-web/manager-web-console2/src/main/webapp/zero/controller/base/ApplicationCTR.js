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
        selector: '#gridApplication'
    },
    {
    	ref: 'gridConfigs' ,
    	selector: '#gridConfigs'
    }],
	init:function () {
		console.log("Load ApplicationCTR");
		var me = this;
		me.control({
			/*
			'#exGrid' : {
				afterrender: function (el) {
					console.log("ExGrid Reder1 ",el,me.getGridApplication().findParentByType('panel'));
					var task = new Ext.util.DelayedTask(function(){
						me.getGridApplication().doLayout();
					});
					task.delay(10);
				}
			},
			*/
			'#gridConfigs button[text=ADD]': {
				click:function (el) {
					var record  = me.getGridApplication().getSelectionModel().getSelection()[0];
					//var row = me.getGridApplication().store.indexOf(record);
					console.log("Config Add",record);
					record.configStore.add({});
				}
			},
			'baseApplication ' : {
				refresh:function () {
					console.log("Refresh Grid");
				},
				afterlayout:function () {
					//console.log("BaseApplication Afterlayout",arguments[0],arguments[1]);
				},
				afterrender:function () {
					var selectionModel = me.getGridApplication().getSelectionModel();
					console.log("afterrender", me, me.getGridConfigs(), me.getGridApplication());
					console.log("SelectionModel " , me.getGridApplication().getSelectionModel());
					var store = me.getBaseApplicationsStore();
					// 행변경 이벤트 처리.
					selectionModel.on('selectionchange',function (el , selected) {
						// 선택건 존재할때 
					
						if(selected.length > 0) { 
							//  해당 행이 SubStore가지고있지 않을 경우.
							var rowRecord = selected[0];
							if(!rowRecord['configStore']) {
								console.log("Load Config Grid Data",rowRecord);
								var recordStore  = Ext.create('Zero.store.base.ApplicationsConfigs');
								rowRecord.addSubStore('configStore',recordStore,'configs');
								recordStore.load({params:{
									app_Id:rowRecord.data.app_Id
								}});
							}
							// 변경행 데이터 반영.
							me.getGridConfigs().suspendEvents();
							me.getGridConfigs().reconfigure(rowRecord['configStore']);
							me.getGridConfigs().resumeEvents();
						}
					});
					
					store.load({
						callback:function () {
							console.log("Init Data Count " , me.getBaseApplicationsStore().count());
							if(me.getBaseApplicationsStore().count() > 0) {
								console.log("Init Select First");
								selectionModel.select(0, true); 
							}
						}
					});
					
					me.getGridApplication().suspendEvents();
					me.getGridApplication().reconfigure(me.getBaseApplicationsStore());
					me.getGridApplication().resumeEvents();
					
					
					
				}
			},
			'#gridApplication button[text=UIREFRESH]': {
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
			'#gridApplication button[text=ADD]': {
				click:function () {
					me.getBaseApplicationsStore().add({});
					console.log("Added Store",me.getBaseApplicationsStore());
					
					
				}
			},
			'baseApplication button[text=SAVE]': {
				click:function () {
					me.getBaseApplicationsStore().sync({
						callback:function (){
							//alert("Reload Data");
							me.getBaseApplicationsStore().load({});
						}
					});
				}
			}
		}
		);
		me.callParent();
	}
});