/**
 * 
 */
Ext.define('Zero.controller.app.InstallerCTR', {
	extend : 'Ext.app.Controller',
	stores : [ 'app.Installers', 'base.Applications', 'app.MonitorProcess' ],
	refs : [ {
		ref : 'appSearchForm',
		selector : '#appSearchForm'
	}, {
		ref : 'gridServers',
		selector : '#gridServers'
	}, {
		ref : 'gridMonitor',
		selector : '#gridMonitor'
	} ],
	views : [ 'app.Installer' ],
	monitorTask : {
		counter : 0,
		interval : 2000
	},
	init : function() {
		var me = this;
		console.log("Load InstallerCTR", me);
		me.control({
			'appInstaller' : {
				afterrender : function(item) {
					var appsCombos = Ext.ComponentQuery.query('#appSearchForm > fieldcontainer > combo');
					item.appsStore.load({
						callback : function() {
							if (appsCombos.length == 1) {
								var appCombo = appsCombos[0];
								console.log("Apply Store");
								var comboStore = appCombo.getStore();
								console.log(comboStore);
								appCombo.setValue(comboStore.getAt(0).get('app_Id'));
								console.log("Proxy Compare", comboStore.getProxy(), me.getAppInstallersStore().getProxy());
							}
						}
					});
					
					Ext.apply(me.monitorTask,{run : function() {
						var taskRunner = this;
						console.log("Run Counter");
							me.getAppMonitorProcessStore().load({
							callback : function(records) {
								if(records.length == 0 && taskRunner.counter > 5) {
									taskRunner.counter = 0;
									Ext.TaskManager.stop(me.monitorTask);
									console.log("Stop Monitor");
								} 
								else if(records.length > 0) {
									taskRunner.counter = 0;
								}
								console.log("Counter Monitor", arguments , taskRunner.counter++);
							}
						});

					},});
					me.getGridServers().suspendEvents();
					me.getGridServers().reconfigure(me.getAppInstallersStore());
					me.getGridServers().resumeEvents();

					me.getGridMonitor().suspendEvents();
					me.getGridMonitor().reconfigure(me.getAppMonitorProcessStore());
					me.getGridMonitor().resumeEvents();

					// Set Installer Monitor TaskRunner;
					Ext.TaskManager.start(me.monitorTask);
					
				}
			},
			'#appSearchForm combo' : {
				change : function(el, val, prevVal) {

					console.log("App Combo Value Change", arguments, me.getAppInstallersStore());
					me.getAppInstallersStore().load({
						params : {
							app_Id : val
						},
						callback : function() {
							console.log("Search Rslt", arguments);
						}
					});
				},
				afterrender : function() {
					console.log("Combo init", arguments, me, me.getAppInstallerView());
					// cb.setValue(cb.getStore().getAt(0).get('app_Id'));
				}
			},
			'#gridServers actioncolumn' : {
				click : function(obj, el, row, col, event, record) {
					if (record.get("install_Flag") == 'F') {

					} else {
						var writer = Ext.create('Ext.data.writer.Json');
						// 설치 설택된 아이템 추출
						
						Ext.Ajax.request({
							url : '/console2/application/installer/install.json',
							headers : {
								'accept' : 'application/json'
							},
							method : 'POST',
							//							params : {
							//								requestParam : 'notInRequestBody'
							//							},
							jsonData : writer.getRecordData(record),
							success : function() {
								console.log('success');
								me.monitorTask.counter = 0;
								Ext.TaskManager.start(me.monitorTask);
							},
							failure : function() {
								console.log('woops');
							}
						});
					}

				}
			},
			'#gridServers button[text=INSTALL_ALL]' : {
				click : function() {
					var writer = Ext.create('Ext.data.writer.Json');
					var sendData = {list:[]}
					// 설치 설택된 아이템 추출
					me.getAppInstallersStore().each(function (record) {
						if(record.data.install_Chk) {
							sendData.list.push(record.data);
						}
					});
					
					if(sendData.list.length > 0) {
						 var  jsonDataEncode = Ext.JSON.encode(sendData);
						 console.log(jsonDataEncode);
						 
						 Ext.Ajax.request({
								url : '/console2/application/installer/install.json',
								headers : {
									'accept' : 'application/json'
								},
								method : 'POST',
								jsonData : jsonDataEncode,
								success : function() {
									console.log('success');
									me.monitorTask.counter = 0;
									Ext.TaskManager.start(me.monitorTask);
								},
								failure : function() {
									console.log('woops');
								}
							});
						 
					}
					
					return ;
					
				}
			},
			'#gridServers button[text=SAVE]' : {
				click : function() {
					me.getAppInstallersStore().sync();
				}
			}
		});
		me.callParent();
	}
});