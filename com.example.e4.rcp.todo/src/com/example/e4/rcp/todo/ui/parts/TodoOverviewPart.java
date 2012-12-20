package com.example.e4.rcp.todo.ui.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.ResourceManager;

import com.example.e4.bundlereresourceloader.IBundleResourceLoader;
import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoOverviewPart {
	private DataBindingContext m_bindingContext;

	@Inject
	private Shell shell;

	@Inject
	private ITodoModel model;

	@Inject
	private IBundleResourceLoader loader;

	@Inject
	private ESelectionService service;

	@Inject
	private EPartService partService;

	@Inject
	private IEventBroker broker;

	private List<Todo> todos;

	private Label lblNewLabel;
	private Table table;
	private TableViewer tableViewer;
	private TableColumn tblclmnSummary;
	private TableViewerColumn tableViewerColumnSummary;
	private TableColumn tblclmnDescription;
	private TableViewerColumn tableViewerColumnDescription;
	private DragSource dragSource;

	// Running external apps in a JFrame?

	/*
	 * @Inject public TodoOverviewPart(Composite parent) {
	 * System.out.println(getClass());
	 * 
	 * System.out.println("Woh! Got Composite via DI.");
	 * 
	 * System.out.println("Layout: " + parent.getLayout().getClass()); }
	 */

	@PostConstruct
	public void createControls(Composite parent) {
		System.out.println("Post Construct called");
		parent.setLayout(new GridLayout(2, false));

		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Job job = new Job("loading") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						List<Todo> list = model.getTodos();

						// Synchronize
						broker.send(MyEventConstants.TOPIC_TODO_DATA_UPDATE
								+ MyEventConstants.UPDATE, list);

						return org.eclipse.core.runtime.Status.OK_STATUS;
					}

				};
				job.schedule();

			}
		});
		btnNewButton.setText("Load Data");

		lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Click to load the data...");

		lblNewLabel.setImage(loader.loadImage(this.getClass(),
				"icons/sample.gif"));

		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection sel = (IStructuredSelection) tableViewer
								.getSelection();
						service.setSelection(sel.getFirstElement());
						MPart part = partService
								.findPart("com.example.e4.rcp.todo.part.tododetails");
						if (part != null) {
							part.setVisible(true);
							partService.showPart(part, PartState.ACTIVATE);

							// TodoDetailsPart d_part=(TodoDetailsPart)
						}

						// System.out.println("coucou selection");
					}
				});
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setComparator(new ViewerComparator() {

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof Todo && e2 instanceof Todo) {
					Todo t1 = (Todo) e1;
					Todo t2 = (Todo) e2;
					return t1.getSummary().compareTo(t2.getSummary());
				}

				else
					return super.compare(viewer, e1, e2);
			}

		});

		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE);

		table = tableViewer.getTable();

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		tableViewerColumnSummary = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnSummary = tableViewerColumnSummary.getColumn();
		tblclmnSummary.setImage(ResourceManager.getPluginImage(
				"com.example.e4.rcp.todo", "icons/sample.gif"));
		tblclmnSummary.setMoveable(true);
		tblclmnSummary.setToolTipText("Coucou And \r\nCoucou");
		tblclmnSummary.setWidth(160);
		tblclmnSummary.setText("Summary");

		tableViewerColumnSummary.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof Todo)
					return ((Todo) element).getSummary();

				return "";
			}

			@Override
			public int getToolTipStyle(Object object) {
				// TODO Auto-generated method stub
				return super.getToolTipStyle(object);
			}

			@Override
			public String getToolTipText(Object element) {
				if (element instanceof Todo)
					return ((Todo) element).getDescription() + "\nTest";
				return super.getToolTipText(element);
			}

			@Override
			public int getToolTipDisplayDelayTime(Object object) {
				// TODO Auto-generated method stub
				return super.getToolTipDisplayDelayTime(object);
			}

			@Override
			public Image getToolTipImage(Object object) {
				return ResourceManager.getPluginImage(
						"com.example.e4.rcp.todo", "icons/save_edit.gif");
				// return super.getToolTipImage(object);
			}

			@Override
			public Point getToolTipShift(Object object) {
				// TODO Auto-generated method stub
				return super.getToolTipShift(object);
			}

			@Override
			public int getToolTipTimeDisplayed(Object object) {
				// TODO Auto-generated method stub
				return super.getToolTipTimeDisplayed(object);
			}

			/*
			 * @Override public boolean useNativeToolTip(Object object) { //
			 * TODO Auto-generated method stub return
			 * super.useNativeToolTip(object); }
			 */

		});

		tableViewerColumnDescription = new TableViewerColumn(tableViewer,
				SWT.NONE);
		tblclmnDescription = tableViewerColumnDescription.getColumn();
		tblclmnDescription.setWidth(166);
		tblclmnDescription.setText("Description");
		tableViewerColumnDescription
				.setLabelProvider(new ColumnLabelProvider() {

					@Override
					public String getText(Object element) {
						if (element instanceof Todo)
							return ((Todo) element).getDescription();

						return "";
					}

					@Override
					public Image getImage(Object element) {
						return ResourceManager.getPluginImage(
								"com.example.e4.rcp.todo",
								"icons/save_edit.gif");

					}

				});

		dragSource = new DragSource(table, DND.DROP_MOVE | DND.DROP_COPY);
		// Provide data in Text format
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		dragSource.setTransfer(types);

		dragSource.addDragListener(new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {
				// Only start the drag if there is actually text in the
				// label - this text will be what is dropped on the target.
				// table.getSelectionIndex()
				if (table.getSelectionIndex() < 0) {
					event.doit = false;
				}

			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// Provide the data of the requested type.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					// event.data = dragLabel.getText();
				}

			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// If a move operation has been performed, remove the data
				// from the source
				if (event.detail == DND.DROP_MOVE) {
					// dragLabel.setText("");
				}

			}
		});
		m_bindingContext = initDataBindings();

		// ITodoModel model = TodoService.getInstance();
		// System.out.println(model.getTodos().size());

	}

	@Inject
	@Optional
	public void reactToTodoDeletion(
			@UIEventTopic(MyEventConstants.TOPIC_TODO_DATA_UPDATE
					+ MyEventConstants.DELETE) Todo todo) {
		// System.out.println("Delete ToDo: " + todo.getDescription());
		if (tableViewer != null) {
			tableViewer.setInput(model.getTodos());
			tableViewer.refresh();
		}

	}

	@Inject
	@Optional
	public void reactToTodoCreation(
			@UIEventTopic(MyEventConstants.TOPIC_TODO_DATA_UPDATE
					+ MyEventConstants.NEW) Todo todo) {
		// System.out.println("Delete ToDo: " + todo.getDescription());
		if (tableViewer != null) {
			tableViewer.setInput(model.getTodos());
			tableViewer.refresh();
		}

	}

	@Inject
	@Optional
	public void reactToTodoUpdateList(
			@UIEventTopic(MyEventConstants.TOPIC_TODO_DATA_UPDATE
					+ MyEventConstants.UPDATE) List<Todo> list) {
		// System.out.println("Update ToDos: " + todo.getDescription());

		todos = list;

		m_bindingContext = initDataBindings();

		/*
		 * if (tableViewer != null) { tableViewer.setInput(list);
		 * tableViewer.refresh(); }
		 */
		if (lblNewLabel != null) {
			lblNewLabel.setText("Number of todos: " + list.size());
		}

	}

	@Focus
	private void setFocus() {
		lblNewLabel.setFocus();
	}

	@PreDestroy
	public void dispose() {
		System.out.println("Pre Destroy called");
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap[] observeMaps = PojoObservables.observeMaps(
				listContentProvider.getKnownElements(), Todo.class,
				new String[] { "summary", "description" });
		tableViewer
				.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tableViewer.setContentProvider(listContentProvider);
		//
		IObservableList selfList = Properties.selfList(Todo.class).observe(
				todos);
		tableViewer.setInput(selfList);
		//
		CellEditor cellEditor = new TextCellEditor(tableViewer.getTable());
		IValueProperty cellEditorProperty = BeanProperties.value("valueValid");
		IBeanValueProperty valueProperty = BeanProperties.value("summary");
		tableViewerColumnSummary
				.setEditingSupport(ObservableValueEditingSupport.create(
						tableViewer, bindingContext, cellEditor,
						cellEditorProperty, valueProperty));
		//
		CellEditor cellEditor_1 = new TextCellEditor(tableViewer.getTable());
		IValueProperty cellEditorProperty_1 = BeanProperties
				.value("valueValid");
		IBeanValueProperty valueProperty_1 = BeanProperties
				.value("description");
		tableViewerColumnDescription
				.setEditingSupport(ObservableValueEditingSupport.create(
						tableViewer, bindingContext, cellEditor_1,
						cellEditorProperty_1, valueProperty_1));
		//
		return bindingContext;
	}
}
