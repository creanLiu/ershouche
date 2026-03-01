export interface Order {
  id: number
  orderNo: string
  customerName: string
  vehicleName: string
  price: number
  status: number
  createdAt: string
  updatedAt: string
  salesId: string
  salesName: string
}

export const orders: Order[] = [
  { id: 1, orderNo: 'ORD20260101001', customerName: '张伟', vehicleName: '大众途观L 2023款', price: 168000, status: 3, createdAt: '2026-01-05', updatedAt: '2026-01-10', salesId: 'admin', salesName: '超级管理员' },
  { id: 2, orderNo: 'ORD20260108002', customerName: '李娜', vehicleName: '丰田凯美瑞 2022款', price: 145000, status: 3, createdAt: '2026-01-08', updatedAt: '2026-01-15', salesId: 'admin', salesName: '超级管理员' },
  { id: 3, orderNo: 'ORD20260112003', customerName: '王芳', vehicleName: '本田CR-V 2023款', price: 178000, status: 2, createdAt: '2026-01-12', updatedAt: '2026-01-20', salesId: 'manager', salesName: '经理' },
  { id: 4, orderNo: 'ORD20260115004', customerName: '刘洋', vehicleName: '宝马3系 2021款', price: 258000, status: 1, createdAt: '2026-01-15', updatedAt: '2026-01-15', salesId: 'sales', salesName: '销售' },
  { id: 5, orderNo: 'ORD20260118005', customerName: '陈静', vehicleName: '奔驰C级 2022款', price: 298000, status: 4, createdAt: '2026-01-18', updatedAt: '2026-01-22', salesId: 'sales', salesName: '销售' },
  { id: 6, orderNo: 'ORD20260120006', customerName: '赵磊', vehicleName: '奥迪A4L 2023款', price: 268000, status: 3, createdAt: '2026-01-20', updatedAt: '2026-01-28', salesId: 'admin', salesName: '超级管理员' },
  { id: 7, orderNo: 'ORD20260125007', customerName: '孙丽', vehicleName: '日产天籁 2022款', price: 138000, status: 2, createdAt: '2026-01-25', updatedAt: '2026-02-01', salesId: 'manager', salesName: '经理' },
  { id: 8, orderNo: 'ORD20260201008', customerName: '周强', vehicleName: '现代途胜L 2023款', price: 158000, status: 1, createdAt: '2026-02-01', updatedAt: '2026-02-01', salesId: 'sales', salesName: '销售' },
  { id: 9, orderNo: 'ORD20260205009', customerName: '吴敏', vehicleName: '起亚K5 2022款', price: 128000, status: 3, createdAt: '2026-02-05', updatedAt: '2026-02-12', salesId: 'admin', salesName: '超级管理员' },
  { id: 10, orderNo: 'ORD20260208010', customerName: '郑华', vehicleName: '福特蒙迪欧 2023款', price: 188000, status: 2, createdAt: '2026-02-08', updatedAt: '2026-02-15', salesId: 'manager', salesName: '经理' },
  { id: 11, orderNo: 'ORD20260210011', customerName: '冯涛', vehicleName: '雪佛兰迈锐宝XL 2022款', price: 118000, status: 1, createdAt: '2026-02-10', updatedAt: '2026-02-10', salesId: 'sales', salesName: '销售' },
  { id: 12, orderNo: 'ORD20260215012', customerName: '蒋雪', vehicleName: '别克君越 2023款', price: 198000, status: 4, createdAt: '2026-02-15', updatedAt: '2026-02-18', salesId: 'admin', salesName: '超级管理员' },
]
