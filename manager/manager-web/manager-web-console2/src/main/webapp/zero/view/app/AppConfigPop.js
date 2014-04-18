Ext.define("Zero.view.app.AppConfigPop", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.appAppConfigPop',
	id : 'appAppConfigPop',
	requires : [ 'Zero.store.base.Applications', 'Zero.ex.grid.BoolCheckColumn', 'Zero.ex.cmp.ComboboxColumn' ],
	controllers : [ 'Zero.controller.app.AppConfigPopCTR' ],
	autoWidth : true,
	param : null,
	gridStore : Ext.create('Zero.store.app.AppConfigPops'),
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
		Ext.apply(param, {
			prop_Type : '99'
		});
		me.param = param;
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
		return Ext.create('Ext.grid.Panel', {
			id : 'gridAppConfigSrc',
			flex : 1,
			store : me.gridStore,
			// store: Ext.create('Zero.store.base.Servers'),
			viewConfig : {
				autoScroll : true
			},
			autoScroll : true,
			autoHeight : true,
			columns : [ {
				xtype : 'boolcheckcolumn',
				header : 'Check',
				checkVal : 'check',
				uncheckVal : '',
				dataIndex : 'chk',
				width:50,
				
			}, {
				header : 'Key',
				dataIndex : 'prop_Key',
				flex : 2,
				field : {
					xtype : 'textfield',
				},
				editor : {
					allowBlank : false
				}
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
				editable : true,
				tooltip : '중요도',
				width:70,
			}, {
				header : 'Description',
				dataIndex : 'prop_Desc',
				flex : 2,
				editable : true,
				field : {
					xtype : 'textfield'
				}
			} ],
			// plugins : [ cellEditingGrid ],
			flex : 1,
		});
	}
});
