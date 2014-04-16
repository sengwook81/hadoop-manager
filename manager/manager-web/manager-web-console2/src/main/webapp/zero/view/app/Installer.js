/**
 * Install 진행 뷰
 */
Ext.define("Zero.view.app.Installer", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.appInstaller',
	requires : [ 'Zero.ex.cmp.ComboboxColumn','Zero.ex.grid.BoolCheckColumn'],
	controllers : [ 'Zero.controller.app.InstallerCTR' ],
	appsStore :  Ext.create('Zero.store.base.Applications'),
	groupServerStore :  Ext.create('Zero.store.base.ServerGroups',{autoLoad:false}),
	initComponent : function() {
		var me = this;
		me.groupServerStore.load();
		me.dataGrid = me.buildGrid();
		me.monitorGrid = me.buildMonitorGrid();
		Ext.apply(me, {
			layout : {
				type : 'vbox', // 폼 패널 레이아웃으로 VBox 레이아웃 사용.
				align : 'stretch' // 아이템들을 폭에 맞도록 늘림.
			},
			items : [ {
				xtype : 'form',
				id:'appSearchForm',
				height : 'auto',
				width : 'auto',
				layout : 'anchor',
				items : [ {
					xtype : 'fieldcontainer',
					items : [ {
						xtype : 'combo',
						fieldLabel : 'Applications',
						store : me.appsStore,
						valueField : 'app_Id',
						triggerAction : 'all',
						editable : false,
						tpl : Ext.create('Ext.XTemplate', '<tpl for=".">', '<div class="x-boundlist-item">{app_Name}-{app_Ver}</div>', '</tpl>'),
						displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">', '{app_Name}-{app_Ver}', '</tpl>'),
					} 
					]
				} ]
			}, me.dataGrid  , me.monitorGrid ]
		});
		me.callParent();
	},
	buildGrid : function() {
		var me = this;
		return Ext.create('Ext.grid.Panel', {
			id : 'gridServers',
			columns : [ {
				xtype : 'boolcheckcolumn',
				header : 'INSTALL',
				checkVal: true,
				uncheckVal : false,
				dataIndex : 'install_Chk',
				flex : 1
			},{
				xtype : 'zcombobox',
				header : 'SERVER GROUP',
				dataIndex : 'group_Id',
				store : me.groupServerStore,
				namefield : 'group_Name',
				codefield : 'group_Id',
				tooltip : '서버그룹',
			}, {
				header : 'NAME',
				dataIndex : 'server_Name',
				flex : 1,
			},
			{
				header : 'OPTIONS',
				dataIndex : 'app_Ext_Opts',
				flex : 1,
			}, {
				xtype : 'zcombobox',
				header : 'STATUS',
				dataIndex : 'install_Flag',
				store : Ext.create('Zero.store.base.InstallStatus'),
				namefield : 'name',
				codefield : 'code',
				tooltip : '설치상태',
			} ,	{
				xtype:'actioncolumn',
				header : 'INSTALL',
			    tooltip: 'Install',
			    align:'center',
			    renderer: function(value , meta,  record , row , col){
			    	console.log(record.get('install_Flag'),record);
			    	if(record.get('install_Flag') != 'F') {
			        return  '<div style="float:left; font-size: 13px; line-height: 1em;">'
	                + 'INSTALL' 
	                + '</div>';
			    	}
			    	else {
			    		 return  '<div style="float:left; font-size: 13px; line-height: 1em;">'
			                + 'UNINSTALL' 
			                + '</div>';
			    	}
			    },
		    	items : [
    	            {
    	                // icon:'some_icon.png',
    	                handler : function (grid, rowIndex, colIndex, item, e, record) {
    	                	console.log("Click ", record);
    	                	this.fireEvent('click', this);
    	                },
    	                scope : me
    	            }
    	        ]
			}
			,{
				header : 'STATUS',
				dataIndex : 'install_Status',
				flex : 1
			} ],
			flex : 1,
			dockedItems : [ {
				xtype : 'toolbar',
				dock : 'top',
				items : [ {
					text : 'REFRESH',
					xtype : 'button',
				}, '->', {
					text : 'INSTALL_ALL',
					xtype : 'button',
				} ]
			} ]
		});
	},buildMonitorGrid : function() {
		var me = this;
		return Ext.create('Ext.grid.Panel', {
			id : 'gridMonitor',
			columns : [ {
				xtype : 'zcombobox',
				header : 'SERVER GROUP',
				dataIndex : 'group_Id',
				store : me.groupServerStore,
				namefield : 'group_Name',
				codefield : 'group_Id',
				tooltip : '서버그룹',
			}, {
				header : 'NAME',
				dataIndex : 'server_Name',
				flex : 1,
			}, {
				xtype : 'zcombobox',
				header : 'APPLICATION',
				dataIndex : 'app_Id',
				store : me.appsStore,
				namefield : 'app_Name',
				codefield : 'app_Id',
				tooltip : 'ApplicationName',
			},{
				header : 'STATUS',
				dataIndex : 'process_Status',
				flex : 1,
			},{
				header : 'DATA',
				dataIndex : 'process_Data',
				flex : 1
			} ],
			flex : 1,
		});
	}
});