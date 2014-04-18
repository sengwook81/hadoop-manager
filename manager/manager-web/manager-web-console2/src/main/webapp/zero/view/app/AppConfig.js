Ext.define("Zero.view.app.AppConfig", {
	//extend : 'Ext.panel.Panel',
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.appAppConfig',
	//id : 'appAppConfig',
	requires : [ 'Zero.store.base.Applications', 'Zero.ex.grid.BoolCheckColumn', 'Zero.ex.cmp.ComboboxColumn' ],
	controllers : [ 'Zero.controller.app.AppConfigCTR' ],
	autoWidth : true,
	//param : null,
	//gridStore : Ext.create('Zero.store.app.AppConfigs'),
	typeStore : Ext.create('Ext.data.Store', {
		fields : [ 'code', 'name' ],
		data : [ {
			code : '01',
			name : '필수'
		}, {
			code : '10',
			name : '추천'
		}, {
			code : '99',
			name : '보통'
		}, ]
	}),
	constructor : function(param) {
		var me = this;
		//
		me.param =param;
		
		if(!param['app_Id']) {
			alert("Apply Temp Data");
			Ext.apply(param , { app_Id:'AA00000014', config_Id:'AC00000032'});
		}
		console.log("Construct Config Window Parameter", me.param);
		Ext.apply(me, param);
		me.callParent();
	},
	initComponent : function() {
		var me = this;
		console.log("InitComponent Config Window Parameter", me.param);

		var propertyGrid = me.buildConfigPropertyGrid();
		Ext.apply(me, {
			layout : {
				type : "vbox",
				align : 'stretch'
			},
			items : [ propertyGrid ]
		});
		me.callParent();
	},
	buildConfigPropertyGrid : function() {
		var me = this;
		var cellEditingGrid = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});
		var store = Ext.create('Zero.store.app.AppConfigs');
		store.load({
			params:me.param
		});
		me.grid =  Ext.create('Ext.grid.Panel', {
			flex : 1,
			itemId:'gridAppConfig',
			store:store,
			// store: Ext.create('Zero.store.base.Servers'),
			viewConfig : {
				autoScroll : true
			},
			autoScroll : true,
			autoHeight : true,
			columns : [ {
				header : 'Key',
				dataIndex : 'prop_Key',
				flex : 2,
				editable : false,
			}, {
				header : 'Value',
				dataIndex : 'prop_Val',
				editable : true,
				flex : 1,
				field : {
					xtype : 'textfield',
				}
			}, {
				xtype : 'zcombobox',
				header : 'Importance',
				dataIndex : 'prop_Type',
				store : me.typeStore,
				namefield : 'name',
				codefield : 'code',
				editable : false,
				tooltip : '중요도'
			}, {
				header : 'Description',
				dataIndex : 'prop_Desc',
				flex : 2,
			}, {
				xtype : 'checkcolumn',
				dataIndex : 'del_Yn',
				stopSelection : false,
				header : 'Remove',
				width : 90
			} ],
			plugins : [ cellEditingGrid ],
			flex : 1,
			dockedItems : [ {
				xtype : 'toolbar',
				dock : 'top',
				items : [ {
					text : 'REFRESH',
					xtype : 'button',
				}, '->', {
					text : 'ADD',
					xtype : 'button'
				}, {
					text : 'SAVE',
					xtype : 'button',
				} ]
			} ]
			
		});
		return me.grid;
	}
});
