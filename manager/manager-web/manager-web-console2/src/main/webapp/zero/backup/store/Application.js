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
		me.typeStore = me.buildConfigTypeStore();
		console.log("TYPE STORE " , me.typeStore);
		
		me.nestedRowExpander = new Ext.grid.plugin.RowExpander({
			selectRowOnExpand: true,
			rowBodyTpl: new Ext.XTemplate('<div class="expanderArea">','</div>',{}),
		});
		me.dataGrid = me.buildGrid();
		Ext.apply(me, {
			layout : 'hbox',
			items : [me.dataGrid],
		});
		me.callParent();
	},
	buildConfigTypeStore: function () {
		return Ext.create('Ext.data.Store', {
			fields:['code','name'],
			data:[
		     {code:'xml',name:'XML'},
		     {code:'yaml',name:'YAML'},
		     {code:'prop',name:'PROPERTY'},
			]
		} );
	} ,
	buildGrid:function () {
		var me = this;
		var typeStroe = me.typeStore;
		var cellEditingGrid = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});
		return Ext.create('Ext.grid.Panel',{
			id : 'gridApplication',
			store: Ext.create('Zero.store.base.Applications'),
			   collapsible: true,
		        animCollapse: false,
			columns : [ {
				header : 'Name',
				dataIndex : 'app_Name',
				flex:1,
				field : {
					xtype : 'textfield'
				}
			}, {
				header : 'Version',
				dataIndex : 'app_Ver',
				flex:1,
				field : {
					xtype : 'textfield'
				}
			},{
				header : 'View_Id',
				dataIndex : 'app_View_Id',
				flex:1,
				field : {
					xtype : 'textfield'
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
			plugins : [ cellEditingGrid , me.nestedRowExpander],
			flex : 1,
			dockedItems : [ {
				xtype : 'toolbar',
				dock:'top',
				items : [ {
					text : 'REFRESH',
					xtype:'button',
				},{
					text : 'UIREFRESH',
					xtype:'button',
				},'->', {
					text : 'ADD',
					xtype:'button',
				}, {
					text : 'SAVE',
					xtype:'button',
				} ]
			} ],
			listeners:{
				
				afterlayout:function (el) {
					if(el,me.dataGrid.getSelectionModel().getSelection() && el,me.dataGrid.getSelectionModel().getSelection().length > 0) {
						var record  = me.dataGrid.getSelectionModel().getSelection()[0];
						var row = me.dataGrid.store.indexOf(record);
						
						expandRow = me.dataGrid.getView().getNodes(row,row);
						
						var expandArea = Ext.get(expandRow).down('.expanderArea');
						console.log("LOG GRID AFTER LAYOUT",row, record,expandRow , expandArea);
						console.log("Check Exists" , expandArea.down('.x-grid-with-row-lines'));
						
						if(expandArea.down('.x-grid-with-row-lines')) 
						{
							
							console.log("Already Exists Grid");
							//var subGrid = expandArea.down('.x-grid-with-row-lines');
							//subGrid.reconfigure(record.configsStore);
							//Ext.get(expandRow).down('.expanderArea').down('div').destroy();	
						}
						else
						{
							if(record.configsStore === undefined) {
								var rowStore = Ext.create('Zero.store.base.ApplicationsConfigs');
								record.configsStore = rowStore;
							}
							
							console.log("Expand Row " , record.configsStore);
							var subGrid = Ext.create('Ext.grid.Panel',{
								//width:500,
								store:record.configsStore,
								itemId:'exGrid',
								columnLines:true,
								autoHeight:true,
								columns:[{
									header:'CONFIG_NAME',
									dataIndex:'config_Name',
									field : {
										xtype : 'textfield'
									}
								},{
									xtype:'zcombobox',
									header : 'CONFIG_TYPE',
									dataIndex : 'config_Type',
									store:typeStroe,
									namefield:'name',
									codefield:'code',
									editable:true,
								    tooltip: '설정타입'
								},{
									 xtype: 'checkcolumn',
									 dataIndex : 'del_Yn',
									 stopSelection : false,
									 header: 'Remove',
									 width: 90
								}],
								plugins:[
					         	Ext.create('Ext.grid.plugin.CellEditing', {
									clicksToEdit : 1
								})],
								dockedItems : [ {
									xtype : 'toolbar',
									dock:'top',
									items : [ '->', {
										text : 'ADD',
										xtype:'button',
									} ]
								} ],
								renderTo:expandArea
							});
							console.log("Draw Again");
							expandArea.swallowEvent(['click', 'mousedown', 'mouseup', 'mousemove', 'dblclick'], true);
							
							expandArea.subgrid = subGrid;
						}
						
					}
				},
				click:function () {
					alert("Grid Click");
				},
				reconfigure:function (el, store, columns, oldStore, The, eOpts) {
					console.log("RECONFIGURE ##########");
				}
			},
			// RowExpander 사용할때는 viewConfig-> listeners 로 등록해야 이벤트 수신됨...
			viewConfig : {
				listeners: {
					refresh:function () {
						console.log("View Refresh");
					},
					reconfigure:function (el, store, columns, oldStore, The, eOpts) {
						console.log("viewConfig RECONFIGURE ##########");
					},
					expandbody : function (rowNode, record, expandRow, eOpts ) {
						var me = this;
						expandRow.isexpand = true;
						var expandArea = Ext.get(expandRow).down('.expanderArea');
						if(expandArea.down('.x-grid-with-row-lines')) 
						{
							//var subGrid = expandArea.down('.x-grid-with-row-lines');
							//subGrid.reconfigure(record.configsStore);
							//Ext.get(expandRow).down('.expanderArea').down('div').destroy();	
						}
						else
						{
							if(record.configsStore === undefined) {
								var rowStore = Ext.create('Zero.store.base.ApplicationsConfigs');
								record.configsStore = rowStore;
							}
							
							console.log("Expand Row " , record.configsStore);
							var subGrid = Ext.create('Ext.grid.Panel',{
								//width:500,
								store:record.configsStore,
								itemId:'exGrid',
								columnLines:true,
								autoHeight:true,
								columns:[{
									header:'CONFIG_NAME',
									dataIndex:'config_Name',
									field : {
										xtype : 'textfield'
									}
								},{
									xtype:'zcombobox',
									header : 'CONFIG_TYPE',
									dataIndex : 'config_Type',
									store:typeStroe,
									namefield:'name',
									codefield:'code',
									editable:true,
								    tooltip: '설정타입'
								},{
									 xtype: 'checkcolumn',
									 dataIndex : 'del_Yn',
									 stopSelection : false,
									 header: 'Remove',
									 width: 90
								}],
								plugins:[
					         	Ext.create('Ext.grid.plugin.CellEditing', {
									clicksToEdit : 1
								})],
								dockedItems : [ {
									xtype : 'toolbar',
									dock:'top',
									items : [ '->', {
										text : 'ADD',
										xtype:'button',
									} ]
								} ],
								renderTo:expandArea
							});
							expandArea.swallowEvent(['click', 'mousedown', 'mouseup', 'mousemove', 'dblclick'], true);
							
							expandArea.subgrid = subGrid;
						}
					},
					collapsebody : function (rowNode, record, expandRow, eOpts ) {
						console.log("Collapse" , rowNode,record,expandRow);
						expandRow.isexpand = false;
						//Ext.get(expandRow).down('.expanderArea').down('div').destroy();
					}, 
				}
			}
		});
	}
});