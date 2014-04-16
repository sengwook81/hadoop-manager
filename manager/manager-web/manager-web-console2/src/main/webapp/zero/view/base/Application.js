/**
 * 서버 관리 뷰
 */
Ext.define("Zero.view.base.Application", {
	extend : 'Zero.view.AbstractZeroView',
	alias : 'widget.baseApplication',
	requires:['Zero.store.base.Applications','Zero.ex.grid.BoolCheckColumn','Zero.ex.cmp.ComboboxColumn'],
	controllers:['Zero.controller.base.ApplicationCTR'],
	
	initComponent : function() {
		var me = this;
		
		var typeStore = Ext.create('Ext.data.Store', {
			fields:['code','name'],
			data:[
		     {code:'xml',name:'XML'},
		     {code:'yaml',name:'YAML'},
		     {code:'prop',name:'PROPERTY'},
			]
		} );
		Ext.apply(me, {
			layout :{
		        type: 'vbox',
		        align: 'stretch'
		    },
			items : [
			me.buildApplicationGrid(),
			me.buildAppConfigsGrid(typeStore)
			]
		});
		me.callParent();
	},
	buildApplicationGrid: function () {
		var me = this;
		var cellEditingGrid = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});
		return Ext.create('Ext.grid.Panel',{
			id : 'gridApplication',
			columns : [ {
				header : 'Name',
				dataIndex : 'app_Name',
				flex:1,
				field : {
					xtype : 'textfield',
					allowBlank:false
				}
			}, {
				header : 'Version',
				dataIndex : 'app_Ver',
				flex:1,
				field : {
					xtype : 'textfield',
					allowBlank:false
				}
			},{
				header : 'ProcessorName',
				dataIndex : 'app_Processor_Name',
				flex:1,
				field : {
					xtype : 'textfield',
					allowBlank:false
				}
			},{
				header : 'Cluster',
				dataIndex : 'cluster_Yn',
				flex:1,
				xtype:'boolcheckcolumn',
				tooltip: '클러스터 지원여부.'
			},{
				 xtype: 'checkcolumn',
				 dataIndex : 'del_Yn',
				 stopSelection : false,
				 header: 'Remove',
				 width: 90
			}],
			plugins : [ cellEditingGrid ],
			flex : 2,
			dockedItems : [ {
				xtype : 'toolbar',
				dock:'top',
				items : [ {
					text : 'REFRESH',
					xtype:'button',
				},'->', {
					text : 'ADD',
					xtype:'button',
				}, {
					text : 'SAVE',
					xtype:'button',
				} ]
			} ]
		});
	},
	buildAppConfigsGrid: function (typeStore) {
		var me = this;
		var cellEditingGrid = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});
		
		return Ext.create('Ext.grid.Panel',{
			id : 'gridConfigs',
			//store: Ext.create('Zero.store.base.Servers'),
			columns:[{
				header:'CONFIG_NAME',
				dataIndex:'config_Name',
				field : {
					xtype : 'textfield',
					allowBlank:false
				}
			},{
				xtype:'zcombobox',
				header : 'CONFIG_TYPE',
				dataIndex : 'config_Type',
				store:typeStore,
				namefield:'name',
				codefield:'code',
				editable:true,
			    tooltip: '설정타입',
			    allowBlank:false
			},
			{
				xtype:'actioncolumn',
				header : 'Opions',
			    tooltip: '팝업',
			    align:'center',
		    	items : [
    	            {
    	                //icon:'some_icon.png',
    	            	html:'Popup',
    	                tooltip : '팝업',
    	                handler : function (grid, rowIndex, colIndex, item, e, record) {
    	                	console.log("Create Popup ", record);
    	                	Ext.create("Ext.window.Window",{
    	                        title : record.data.config_Name,
    	                        width : 800,
    	                        closable : true,
    	                        height: 500,
    	                        layout:'fit',
    	                        ownerCt : this,
    	                        modal : true,
    	                        items: Ext.create('Zero.view.base.ConfigProperties',
    	                        	{
    	                        		app_Id:record.data.app_Id,
    	                        		config_Id:record.data.config_Id
    	                        	})
    	                	}
    	                    ).show();
    	                },
    	                scope : me
    	            }
    	        ]
			}
			,{
				 xtype: 'checkcolumn',
				 dataIndex : 'del_Yn',
				 stopSelection : false,
				 header: 'Remove',
				 width: 90
			}],
			plugins : [ cellEditingGrid ],
			flex : 1,
			dockedItems : [ {
				xtype : 'toolbar',
				dock:'top',
				items : [ '->', {
					text : 'ADD',
					xtype:'button',
				}]
			} ]
		});
	}
});