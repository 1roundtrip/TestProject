import { createBrowserRouter, Navigate } from 'react-router-dom'
import { lazy } from 'react'
import Layout from '@/layouts'
import AuthGuard from '@/router/AuthGuard'

// 懒加载页面
const Login = lazy(() => import('@/pages/Login'))
const Dashboard = lazy(() => import('@/pages/Dashboard'))
const Asset = lazy(() => import('@/pages/Asset'))
const AssetArchive = lazy(() => import('@/pages/Asset/Archive'))
const AssetStorage = lazy(() => import('@/pages/Asset/Storage'))
const AssetBorrow = lazy(() => import('@/pages/Asset/Borrow'))
const AssetTransfer = lazy(() => import('@/pages/Asset/Transfer'))
const AssetDepreciation = lazy(() => import('@/pages/Asset/Depreciation'))
const AssetInventory = lazy(() => import('@/pages/Asset/Inventory'))
const AssetScrap = lazy(() => import('@/pages/Asset/Scrap'))
const AssetReport = lazy(() => import('@/pages/Asset/Report'))
const Purchase = lazy(() => import('@/pages/Purchase'))
const PurchasePlan = lazy(() => import('@/pages/Purchase/Plan'))
const PurchaseRequisition = lazy(() => import('@/pages/Purchase/Requisition'))
const PurchaseSupplier = lazy(() => import('@/pages/Purchase/Supplier'))
const PurchaseOrder = lazy(() => import('@/pages/Purchase/Order'))
const PurchaseContract = lazy(() => import('@/pages/Purchase/Contract'))
const PurchaseReceiving = lazy(() => import('@/pages/Purchase/Receiving'))
const PurchaseQuality = lazy(() => import('@/pages/Purchase/Quality'))
const PurchaseReturn = lazy(() => import('@/pages/Purchase/Return'))
const PurchasePayment = lazy(() => import('@/pages/Purchase/Payment'))
const PurchaseReport = lazy(() => import('@/pages/Purchase/Report'))
const Maintenance = lazy(() => import('@/pages/Maintenance'))
const MaintenanceWorkOrder = lazy(() => import('@/pages/Maintenance/WorkOrder'))
const MaintenancePlan = lazy(() => import('@/pages/Maintenance/Plan'))
const MaintenanceTeam = lazy(() => import('@/pages/Maintenance/Team'))
const MaintenancePart = lazy(() => import('@/pages/Maintenance/Part'))
const MaintenanceQuality = lazy(() => import('@/pages/Maintenance/Quality'))
const MaintenanceCost = lazy(() => import('@/pages/Maintenance/Cost'))
const MaintenanceFault = lazy(() => import('@/pages/Maintenance/Fault'))
const MaintenancePerformance = lazy(() => import('@/pages/Maintenance/Performance'))
const MaintenanceReport = lazy(() => import('@/pages/Maintenance/Report'))
const MaintenanceMobile = lazy(() => import('@/pages/Maintenance/Mobile'))
const Repair = lazy(() => import('@/pages/Repair'))
const Inventory = lazy(() => import('@/pages/Inventory'))
const InventoryWarehouse = lazy(() => import('@/pages/Inventory/Warehouse'))
const InventoryLocation = lazy(() => import('@/pages/Inventory/Location'))
const InventoryMaterial = lazy(() => import('@/pages/Inventory/Material'))
const InventoryInbound = lazy(() => import('@/pages/Inventory/Inbound'))
const InventoryOutbound = lazy(() => import('@/pages/Inventory/Outbound'))
const InventoryTransfer = lazy(() => import('@/pages/Inventory/Transfer'))
const InventoryAdjustment = lazy(() => import('@/pages/Inventory/Adjustment'))
const InventoryStocktaking = lazy(() => import('@/pages/Inventory/Stocktaking'))
const InventoryWarning = lazy(() => import('@/pages/Inventory/Warning'))
const InventoryReport = lazy(() => import('@/pages/Inventory/Report'))
const Warning = lazy(() => import('@/pages/Warning'))
const WarningRule = lazy(() => import('@/pages/Warning/Rule'))
const WarningMonitor = lazy(() => import('@/pages/Warning/Monitor'))
const WarningNotification = lazy(() => import('@/pages/Warning/Notification'))
const WarningTracking = lazy(() => import('@/pages/Warning/Tracking'))
const WarningStatistics = lazy(() => import('@/pages/Warning/Statistics'))
const WarningLevel = lazy(() => import('@/pages/Warning/Level'))
const WarningTemplate = lazy(() => import('@/pages/Warning/Template'))
const WarningChannel = lazy(() => import('@/pages/Warning/Channel'))
const WarningDashboard = lazy(() => import('@/pages/Warning/Dashboard'))
const WarningReport = lazy(() => import('@/pages/Warning/Report'))
const Profile = lazy(() => import('@/pages/Profile'))
const SystemUser = lazy(() => import('@/pages/System/User'))
const SystemRole = lazy(() => import('@/pages/System/Role'))
const SystemMenu = lazy(() => import('@/pages/System/Menu'))

export const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/',
    element: (
      <AuthGuard>
        <Layout />
      </AuthGuard>
    ),
    children: [
      {
        index: true,
        element: <Navigate to="/dashboard" replace />,
      },
      {
        path: 'dashboard',
        element: <Dashboard />,
      },
      {
        path: 'asset',
        element: <Asset />,
        children: [
          {
            index: true,
            element: <Navigate to="/asset/archive" replace />,
          },
          {
            path: 'archive',
            element: <AssetArchive />,
          },
          {
            path: 'storage',
            element: <AssetStorage />,
          },
          {
            path: 'borrow',
            element: <AssetBorrow />,
          },
          {
            path: 'transfer',
            element: <AssetTransfer />,
          },
          {
            path: 'repair',
            element: <Repair />,
          },
          {
            path: 'depreciation',
            element: <AssetDepreciation />,
          },
          {
            path: 'inventory',
            element: <AssetInventory />,
          },
          {
            path: 'scrap',
            element: <AssetScrap />,
          },
          {
            path: 'report',
            element: <AssetReport />,
          },
        ],
      },
      {
        path: 'purchase',
        element: <Purchase />,
        children: [
          {
            index: true,
            element: <Navigate to="/purchase/plan" replace />,
          },
          {
            path: 'plan',
            element: <PurchasePlan />,
          },
          {
            path: 'requisition',
            element: <PurchaseRequisition />,
          },
          {
            path: 'supplier',
            element: <PurchaseSupplier />,
          },
          {
            path: 'order',
            element: <PurchaseOrder />,
          },
          {
            path: 'contract',
            element: <PurchaseContract />,
          },
          {
            path: 'receiving',
            element: <PurchaseReceiving />,
          },
          {
            path: 'quality',
            element: <PurchaseQuality />,
          },
          {
            path: 'return',
            element: <PurchaseReturn />,
          },
          {
            path: 'payment',
            element: <PurchasePayment />,
          },
          {
            path: 'report',
            element: <PurchaseReport />,
          },
        ],
      },
      {
        path: 'maintenance',
        element: <Maintenance />,
        children: [
          {
            index: true,
            element: <Navigate to="/maintenance/work-order" replace />,
          },
          {
            path: 'work-order',
            element: <MaintenanceWorkOrder />,
          },
          {
            path: 'plan',
            element: <MaintenancePlan />,
          },
          {
            path: 'team',
            element: <MaintenanceTeam />,
          },
          {
            path: 'part',
            element: <MaintenancePart />,
          },
          {
            path: 'quality',
            element: <MaintenanceQuality />,
          },
          {
            path: 'cost',
            element: <MaintenanceCost />,
          },
          {
            path: 'fault',
            element: <MaintenanceFault />,
          },
          {
            path: 'performance',
            element: <MaintenancePerformance />,
          },
          {
            path: 'report',
            element: <MaintenanceReport />,
          },
          {
            path: 'mobile',
            element: <MaintenanceMobile />,
          },
        ],
      },
      {
        path: 'repair',
        element: <Repair />,
      },
      {
        path: 'inventory',
        element: <Inventory />,
        children: [
          {
            index: true,
            element: <Navigate to="/inventory/warehouse" replace />,
          },
          {
            path: 'warehouse',
            element: <InventoryWarehouse />,
          },
          {
            path: 'location',
            element: <InventoryLocation />,
          },
          {
            path: 'material',
            element: <InventoryMaterial />,
          },
          {
            path: 'inbound',
            element: <InventoryInbound />,
          },
          {
            path: 'outbound',
            element: <InventoryOutbound />,
          },
          {
            path: 'transfer',
            element: <InventoryTransfer />,
          },
          {
            path: 'adjustment',
            element: <InventoryAdjustment />,
          },
          {
            path: 'stocktaking',
            element: <InventoryStocktaking />,
          },
          {
            path: 'warning',
            element: <InventoryWarning />,
          },
          {
            path: 'report',
            element: <InventoryReport />,
          },
        ],
      },
      {
        path: 'warning',
        element: <Warning />,
        children: [
          {
            index: true,
            element: <Navigate to="/warning/rule" replace />,
          },
          {
            path: 'rule',
            element: <WarningRule />,
          },
          {
            path: 'monitor',
            element: <WarningMonitor />,
          },
          {
            path: 'notification',
            element: <WarningNotification />,
          },
          {
            path: 'tracking',
            element: <WarningTracking />,
          },
          {
            path: 'statistics',
            element: <WarningStatistics />,
          },
          {
            path: 'level',
            element: <WarningLevel />,
          },
          {
            path: 'template',
            element: <WarningTemplate />,
          },
          {
            path: 'channel',
            element: <WarningChannel />,
          },
          {
            path: 'dashboard',
            element: <WarningDashboard />,
          },
          {
            path: 'report',
            element: <WarningReport />,
          },
        ],
      },
      {
        path: 'profile',
        element: <Profile />,
      },
      {
        path: 'system',
        children: [
          {
            path: 'user',
            element: <SystemUser />,
          },
          {
            path: 'role',
            element: <SystemRole />,
          },
          {
            path: 'menu',
            element: <SystemMenu />,
          },
        ],
      },
    ],
  },
])

