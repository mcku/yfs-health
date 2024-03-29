package com.varun.yfs.client.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.admin.rpc.StoreLoaderAsync;
import com.varun.yfs.client.images.YfsImageBundle;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportDetailServiceAsync;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.client.screening.export.ExportService;
import com.varun.yfs.client.screening.export.ExportServiceAsync;
import com.varun.yfs.dto.ClinicDTO;
import com.varun.yfs.dto.ExportTableDTO;
import com.varun.yfs.dto.ExportTableDataDTO;

public class ClinicScreeningReport extends LayoutContainer
{
	private ReportDetailServiceAsync reportDetailService = GWT.create(ReportDetailService.class);
	private StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private LabelField lblfldTotalScreened;
	private Grid<ExportTableDataDTO> gridBreakupOfTreatments;

	private ExportServiceAsync exportServiceAsync = GWT.create(ExportService.class);
	private ComboBox<ModelData> clinics;

	final Listener<MessageBoxEvent> DUMMYLISTENER = new Listener<MessageBoxEvent>()
	{
		@Override
		public void handleEvent(MessageBoxEvent ce)
		{
		}
	};

	public ClinicScreeningReport()
	{
		setHeight("700");
	}

	@Override
	protected void onRender(Element parent, int index)
	{

		super.onRender(parent, index);

		ContentPanel cpOuterContainer = new ContentPanel();
		cpOuterContainer.setHeading("Clinic Screening Report");
		add(cpOuterContainer);

		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new TableLayout(5));

		final DateField dtfldFromDate = new DateField();
		dtfldFromDate.setFieldLabel("From Date");
		dtfldFromDate.setAllowBlank(false);
		Date currentDate = new Date();
		dtfldFromDate.setValue(currentDate);
		LayoutContainer frmpnlFromDate = new LayoutContainer();
		frmpnlFromDate.setLayout(new FormLayout());
		frmpnlFromDate.add(dtfldFromDate, new FormData("100%"));
		TableData td_frmpnlFromDate = new TableData();
		td_frmpnlFromDate.setPadding(5);
		td_frmpnlFromDate.setMargin(5);
		layoutContainer.add(frmpnlFromDate, td_frmpnlFromDate);

		final DateField dtfldToDate = new DateField();
		dtfldToDate.setFieldLabel("To Date");
		dtfldToDate.setAllowBlank(false);
		dtfldToDate.setValue(currentDate);
		LayoutContainer frmpnlToDate = new LayoutContainer();
		frmpnlToDate.setLayout(new FormLayout());
		frmpnlToDate.add(dtfldToDate, new FormData("100%"));
		TableData td_frmpnlToDate = new TableData();
		td_frmpnlToDate.setPadding(5);
		td_frmpnlToDate.setMargin(5);
		layoutContainer.add(frmpnlToDate, td_frmpnlToDate);

		clinics = new ComboBox<ModelData>();
		clinics.setFieldLabel("Select a Clinic");
		clinics.setStore(new ListStore<ModelData>());
		clinics.setDisplayField("clinicName");
		clinics.setValueField("clinicId");
		clinics.setAllowBlank(false);
		storeLoader.getModel(ModelDataEnum.Clinic.name(), new AsyncCallback<ModelData>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				MessageBox.info("Error", "Error encountered while loading report params.", DUMMYLISTENER);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ModelData result)
			{
				clinics.getStore().add((List<ModelData>) result.get("data"));
			}
		});
		LayoutContainer frmpnlClinics = new LayoutContainer();
		frmpnlClinics.setLayout(new FormLayout());
		frmpnlClinics.add(clinics, new FormData("100%"));
		TableData td_frmpnlClinics = new TableData();
		td_frmpnlClinics.setPadding(5);
		td_frmpnlClinics.setMargin(5);
		layoutContainer.add(frmpnlClinics, td_frmpnlClinics);

		LayoutContainer frmpnlRefresh = new LayoutContainer();
		frmpnlRefresh.setLayout(new FormLayout());
		frmpnlRefresh.setBorders(true);
		Button btnRefresh = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.refreshButtonIcon()));
		frmpnlRefresh.add(btnRefresh, new FormData("100%"));
		btnRefresh.setToolTip("Generate Report");
		TableData td_frmpnlRefresh = new TableData();
		td_frmpnlRefresh.setPadding(5);
		td_frmpnlRefresh.setMargin(5);
		layoutContainer.add(frmpnlRefresh, td_frmpnlRefresh);

		LayoutContainer frmpnlExport = new LayoutContainer();
		frmpnlExport.setLayout(new FormLayout());
		frmpnlExport.setBorders(true);
		Button btnExport = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.excelExportIcon()));
		frmpnlExport.add(btnExport, new FormData("100%"));
		btnExport.setToolTip("Export Report");
		TableData td_frmpnlExport = new TableData();
		td_frmpnlExport.setPadding(5);
		td_frmpnlExport.setMargin(5);
		layoutContainer.add(frmpnlExport, td_frmpnlExport);

		add(layoutContainer);

		final FormPanel formPanel = new FormPanel();
		final HiddenField<String> exportedFileName = new HiddenField<String>();
		exportedFileName.setName("ExportedFilename");
		formPanel.add(exportedFileName);
		formPanel.setVisible(false);
		add(formPanel);

		btnExport.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if (!validateReportParams(dtfldFromDate, dtfldToDate))
					return;

				mask("Please wait.Generating Report...");
				ModelData model = new BaseModelData();
				model.set("dateFrom", dtfldFromDate.getValue().getTime());
				model.set("dateTo", dtfldToDate.getValue().getTime());
				model.set("clinicId", ((ClinicDTO) clinics.getSelection().get(0)).getId());
				reportDetailService.getModel(ReportType.Clinic, model, new AsyncCallback<ModelData>()
				{
					@Override
					public void onSuccess(ModelData result)
					{
						decodeResult(result);

						List<ExportTableDTO> lstExportTableDto = new ArrayList<ExportTableDTO>();
						List<String> headers = new ArrayList<String>();
						List<String> headerTags = new ArrayList<String>();
						List<ColumnConfig> columns = gridBreakupOfTreatments.getColumnModel().getColumns();
						List<ExportTableDataDTO> models = gridBreakupOfTreatments.getStore().getModels();

						List<String> addlData = new ArrayList<String>();
						addlData.add(lblfldTotalScreened.getText());

						for (ColumnConfig columnConfig : columns)
						{
							headers.add(columnConfig.getHeader());
							headerTags.add(columnConfig.getId());
						}
						ExportTableDTO expTab = new ExportTableDTO();
						expTab.setColHeaders(headers);
						expTab.setLstData(models);
						expTab.setColHeaderTags(headerTags);
						expTab.setAddlData(addlData);
						lstExportTableDto.add(expTab);

						exportServiceAsync.createExportFile(lstExportTableDto, null, new AsyncCallback<String>()
						{
							@Override
							public void onFailure(Throwable caught)
							{
								unmask();
								MessageBox.alert("Alert", "Error encountered while exporting." + caught.getMessage(),
										DUMMYLISTENER);
							}

							@Override
							public void onSuccess(String result)
							{
								exportedFileName.setValue(result);

								String url = GWT.getModuleBaseURL();
								url = url + "exportServlet";

								formPanel.setAction(url);
								formPanel.submit();
								unmask();
							}

						});
					}

					@Override
					public void onFailure(Throwable caught)
					{
						unmask();
						MessageBox.info("Error", "Error encountered while loading the report." + caught.getMessage(),
								DUMMYLISTENER);
					}
				});

			}
		});

		btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if (!validateReportParams(dtfldFromDate, dtfldToDate))
					return;

				ModelData model = new BaseModelData();
				model.set("dateFrom", dtfldFromDate.getValue().getTime());
				model.set("dateTo", dtfldToDate.getValue().getTime());
				model.set("clinicId", ((ClinicDTO) clinics.getSelection().get(0)).getId());
				reportDetailService.getModel(ReportType.Clinic, model, new AsyncCallback<ModelData>()
				{
					@Override
					public void onSuccess(ModelData result)
					{
						decodeResult(result);
					}

					@Override
					public void onFailure(Throwable caught)
					{
						MessageBox.info("Error", "Error encountered while loading the report." + caught.getMessage(),
								DUMMYLISTENER);
					}
				});
			}
		});

		FormPanel lcReportingParams = new FormPanel();
		lcReportingParams.setHeaderVisible(false);
		lcReportingParams.setSize("", "700");

		lblfldTotalScreened = new LabelField("Total Number Screened:");
		lcReportingParams.add(lblfldTotalScreened, new FormData("100%"));

		List<ColumnConfig> configsBreakupOfTreatments = new ArrayList<ColumnConfig>();

		ColumnConfig breakupOfTreatment = new ColumnConfig("breakUpOfTreatment", "Breakup of Treatments", 80);
		configsBreakupOfTreatments.add(breakupOfTreatment);

		ColumnConfig screened = new ColumnConfig("screened", "Total Screened", 60);
		configsBreakupOfTreatments.add(screened);

		ColumnConfig casesClosed = new ColumnConfig("surgeryCasesClosed", "Surgery Cases Closed", 60);
		configsBreakupOfTreatments.add(casesClosed);

		ColumnConfig medicineClosedCases = new ColumnConfig("medicineCasesClosed", "Medicine Cases Closed", 80);
		configsBreakupOfTreatments.add(medicineClosedCases);

		ColumnConfig pendingSurgeryCases = new ColumnConfig("pendingSurgeryCases", "Pending Surgery Cases", 80);
		configsBreakupOfTreatments.add(pendingSurgeryCases);

		ColumnConfig followUpMedicines = new ColumnConfig("followUpMedicines", "Follow Up Medicines", 80);
		configsBreakupOfTreatments.add(followUpMedicines);

		ColumnConfig closedSurgeryCases = new ColumnConfig("closedSurgeryCases", "Surgery Cases Closed", 80);
		configsBreakupOfTreatments.add(closedSurgeryCases);

		ColumnConfig caseClosed = new ColumnConfig("caseClosed", "Case Closed", 80);
		configsBreakupOfTreatments.add(caseClosed);

		ColumnConfig referredToHospital = new ColumnConfig("referredToHospital", "Referred To Hospital", 80);
		configsBreakupOfTreatments.add(referredToHospital);

		ColumnConfig pendingCases = new ColumnConfig("pendingCases", "Pending Cases", 80);
		configsBreakupOfTreatments.add(pendingCases);

		gridBreakupOfTreatments = new Grid<ExportTableDataDTO>(new ListStore<ExportTableDataDTO>(), new ColumnModel(
				configsBreakupOfTreatments));
		gridBreakupOfTreatments.setHeight("350");
		gridBreakupOfTreatments.setBorders(true);
		gridBreakupOfTreatments.setColumnLines(true);
		gridBreakupOfTreatments.getView().setAutoFill(true);

		// List<ColumnConfig> configsBreakupOfTreatments = new
		// ArrayList<ColumnConfig>();
		//
		// ColumnConfig clmncnfgNewColumn_4 = new
		// ColumnConfig("nonSurgicalCases", "Non-Surgical Cases", 150);
		// configsBreakupOfTreatments.add(clmncnfgNewColumn_4);
		//
		// ColumnConfig clmncnfgNewColumn_5 = new ColumnConfig("noOfCases",
		// "No. Of Cases", 150);
		// configsBreakupOfTreatments.add(clmncnfgNewColumn_5);
		//
		// gridNonSurgeryCases = new Grid<ModelData>(new ListStore<ModelData>(),
		// new ColumnModel(configsBreakupOfTreatments));
		// gridNonSurgeryCases.setBorders(true);

		FormData fd_gridStatusOfTreatment = new FormData("100%");
		fd_gridStatusOfTreatment.setMargins(new Margins(0, 0, 5, 0));
		lcReportingParams.add(gridBreakupOfTreatments, fd_gridStatusOfTreatment);
		// FormData fd_gridBreakupOfTreatments = new FormData("100%");
		// fd_gridBreakupOfTreatments.setMargins(new Margins(0, 0, 5, 0));
		// lcReportingParams.add(gridNonSurgeryCases,
		// fd_gridBreakupOfTreatments);
		// gridNonSurgeryCases.setHeight("150");

		lcReportingParams.setLayoutData(new Margins(5, 5, 5, 5));
		add(lcReportingParams);

	}

	private boolean validateReportParams(final DateField dtfldFromDate, final DateField dtfldToDate)
	{
		if (dtfldFromDate.getValue() == null)
		{
			MessageBox.info("Report Parameter Needed", "From-Date field cannot be empty", DUMMYLISTENER);
			return false;
		}
		if (dtfldToDate.getValue() == null)
		{
			MessageBox.info("Report Parameter Needed", "To-Date field cannot be empty", DUMMYLISTENER);
			return false;
		}
		if (clinics.getSelection().isEmpty())
		{
			MessageBox.info("Report Parameter Needed", "Please select a Clinic", DUMMYLISTENER);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void decodeResult(ModelData result)
	{
		lblfldTotalScreened.clear();
		lblfldTotalScreened.setText("Total Number Screened:" + result.get("locationsCount"));

		gridBreakupOfTreatments.getStore().removeAll();
		gridBreakupOfTreatments.getStore().add((List<? extends ExportTableDataDTO>) result.get("breakupOfTreatments"));

		// final ListStore<ChartData> store = new ListStore<ChartData>();
		// Integer screened, surgeryCaseClosed, pendingCases, followUpMedicines,
		// referredToHospital;
		// for (ModelData model :
		// gridBreakupOfTreatments.getStore().getModels())
		// {
		// String breakupOfTreatment =
		// model.get("breakUpOfTreatment").toString();
		//
		// Object obj = model.get("screened");
		// screened = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("surgeryCasesClosed");
		// surgeryCaseClosed = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("pendingCases");
		// pendingCases = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("followUpMedicines");
		// followUpMedicines = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("referredToHospital");
		// referredToHospital = (obj == null) ? 0 : (Integer) obj;
		//
		// ChartData tmSales = new ChartData(breakupOfTreatment, screened,
		// surgeryCaseClosed, pendingCases, followUpMedicines,
		// referredToHospital);
		// store.add(tmSales);
		// }
	}
}
