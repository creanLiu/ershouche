export interface VehicleImage {
  url: string
  isMain: boolean
}

export interface Vehicle {
  id: number
  vehicleNo: string
  brand: string
  model: string
  year: number
  price: number
  mileage: number
  color: string
  transmission: '自动' | '手动'
  displacement: string
  city: string
  status: 0 | 1
  isSpecial: boolean
  coverImageUrl: string
  images: VehicleImage[]
  vin: string
  description: string
  registrationDate: string
  insuranceExpiry: string
  // 超管专属字段
  purchasePrice: number
  refurbishCost: number
  finalCost: number
}

const placeholder = (w: number, h: number) =>
  `https://placehold.co/${w}x${h}/e8e8e8/999?text=Car`

export const mockVehicles: Vehicle[] = [
  { id: 1, vehicleNo: 'C001', brand: '丰田', model: '凯美瑞', year: 2021, price: 168000, mileage: 32000, color: '珍珠白', transmission: '自动', displacement: '2.5L', city: '上海', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }, { url: placeholder(400, 300), isMain: false }], vin: 'JTDBE32K123456001', description: '一手车，保养记录完整，无事故', registrationDate: '2021-03-15', insuranceExpiry: '2027-03-14', purchasePrice: 140000, refurbishCost: 3000, finalCost: 143000 },
  { id: 2, vehicleNo: 'C002', brand: '本田', model: '雅阁', year: 2020, price: 145000, mileage: 48000, color: '星空灰', transmission: '自动', displacement: '1.5T', city: '北京', status: 1, isSpecial: true, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }, { url: placeholder(400, 300), isMain: false }], vin: 'JHMCM56557C123002', description: '家用代步，车况良好', registrationDate: '2020-06-20', insuranceExpiry: '2026-06-19', purchasePrice: 118000, refurbishCost: 2500, finalCost: 120500 },
  { id: 3, vehicleNo: 'C003', brand: '大众', model: '帕萨特', year: 2019, price: 128000, mileage: 65000, color: '深海蓝', transmission: '自动', displacement: '1.8T', city: '广州', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WVWZZZ3CZ9E123003', description: '商务用车，内饰整洁', registrationDate: '2019-09-10', insuranceExpiry: '2026-09-09', purchasePrice: 100000, refurbishCost: 4000, finalCost: 104000 },
  { id: 4, vehicleNo: 'C004', brand: '宝马', model: '3系', year: 2022, price: 298000, mileage: 18000, color: '矿石白', transmission: '自动', displacement: '2.0T', city: '深圳', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }, { url: placeholder(400, 300), isMain: false }], vin: 'WBA8E9C50JA123004', description: '准新车，全原厂配置', registrationDate: '2022-01-08', insuranceExpiry: '2027-01-07', purchasePrice: 255000, refurbishCost: 1500, finalCost: 256500 },
  { id: 5, vehicleNo: 'C005', brand: '奔驰', model: 'C级', year: 2021, price: 325000, mileage: 25000, color: '曜岩黑', transmission: '自动', displacement: '1.5T', city: '成都', status: 1, isSpecial: true, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WDD2050561R123005', description: '豪华版，全景天窗', registrationDate: '2021-07-22', insuranceExpiry: '2026-07-21', purchasePrice: 285000, refurbishCost: 2000, finalCost: 287000 },
  { id: 6, vehicleNo: 'C006', brand: '奥迪', model: 'A4L', year: 2020, price: 235000, mileage: 41000, color: '冰川白', transmission: '自动', displacement: '2.0T', city: '杭州', status: 0, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WAUZZZ8K9BA123006', description: 'quattro四驱，运动套件', registrationDate: '2020-04-18', insuranceExpiry: '2026-04-17', purchasePrice: 195000, refurbishCost: 3500, finalCost: 198500 },
  { id: 7, vehicleNo: 'C007', brand: '丰田', model: '汉兰达', year: 2021, price: 268000, mileage: 38000, color: '银色', transmission: '自动', displacement: '2.0T', city: '武汉', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'JTMZD33V975123007', description: '七座SUV，适合家用', registrationDate: '2021-05-30', insuranceExpiry: '2026-05-29', purchasePrice: 228000, refurbishCost: 2800, finalCost: 230800 },
  { id: 8, vehicleNo: 'C008', brand: '本田', model: 'CR-V', year: 2022, price: 198000, mileage: 15000, color: '晶耀红', transmission: '自动', displacement: '1.5T', city: '南京', status: 1, isSpecial: true, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'JHLRM4H74CC123008', description: '混动版，油耗极低', registrationDate: '2022-03-12', insuranceExpiry: '2027-03-11', purchasePrice: 168000, refurbishCost: 1200, finalCost: 169200 },
  { id: 9, vehicleNo: 'C009', brand: '大众', model: '途观L', year: 2020, price: 178000, mileage: 52000, color: '深棕色', transmission: '自动', displacement: '2.0T', city: '西安', status: 0, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WVGZZZ5NZ9M123009', description: '四驱版，越野性能好', registrationDate: '2020-08-25', insuranceExpiry: '2026-08-24', purchasePrice: 145000, refurbishCost: 4500, finalCost: 149500 },
  { id: 10, vehicleNo: 'C010', brand: '宝马', model: 'X3', year: 2021, price: 368000, mileage: 22000, color: '地中海蓝', transmission: '自动', displacement: '2.0T', city: '重庆', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WBA8E9C50JA123010', description: 'xDrive28i，M运动套装', registrationDate: '2021-11-05', insuranceExpiry: '2026-11-04', purchasePrice: 320000, refurbishCost: 2200, finalCost: 322200 },
  { id: 11, vehicleNo: 'C011', brand: '奔驰', model: 'E级', year: 2020, price: 415000, mileage: 35000, color: '钯银色', transmission: '自动', displacement: '2.0T', city: '天津', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WDD2130561A123011', description: '豪华商务，后排空间大', registrationDate: '2020-12-01', insuranceExpiry: '2026-11-30', purchasePrice: 365000, refurbishCost: 3200, finalCost: 368200 },
  { id: 12, vehicleNo: 'C012', brand: '奥迪', model: 'Q5L', year: 2022, price: 388000, mileage: 12000, color: '冰川白', transmission: '自动', displacement: '2.0T', city: '苏州', status: 1, isSpecial: true, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WAUZZZ8R9CA123012', description: '准新车，原厂保修中', registrationDate: '2022-06-18', insuranceExpiry: '2027-06-17', purchasePrice: 340000, refurbishCost: 1800, finalCost: 341800 },
  { id: 13, vehicleNo: 'C013', brand: '丰田', model: '卡罗拉', year: 2019, price: 88000, mileage: 72000, color: '珍珠白', transmission: '手动', displacement: '1.6L', city: '郑州', status: 0, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'JTDBL40E299123013', description: '经济实用，适合新手', registrationDate: '2019-02-14', insuranceExpiry: '2026-02-13', purchasePrice: 68000, refurbishCost: 5000, finalCost: 73000 },
  { id: 14, vehicleNo: 'C014', brand: '本田', model: '思域', year: 2021, price: 118000, mileage: 28000, color: '烟熏蓝', transmission: '手动', displacement: '1.5T', city: '长沙', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'JHMFC1B32MA123014', description: '运动版，改装潜力大', registrationDate: '2021-09-08', insuranceExpiry: '2026-09-07', purchasePrice: 95000, refurbishCost: 2000, finalCost: 97000 },
  { id: 15, vehicleNo: 'C015', brand: '大众', model: '高尔夫', year: 2020, price: 135000, mileage: 44000, color: '钢铁灰', transmission: '自动', displacement: '1.4T', city: '青岛', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WVWZZZ1KZ9W123015', description: 'GTI外观套件，驾驶感强', registrationDate: '2020-10-20', insuranceExpiry: '2026-10-19', purchasePrice: 108000, refurbishCost: 3000, finalCost: 111000 },
  { id: 16, vehicleNo: 'C016', brand: '宝马', model: '5系', year: 2021, price: 468000, mileage: 30000, color: '矿石白', transmission: '自动', displacement: '2.0T', city: '沈阳', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WBA5A7C50ED123016', description: '行政版，配置齐全', registrationDate: '2021-04-15', insuranceExpiry: '2026-04-14', purchasePrice: 415000, refurbishCost: 2500, finalCost: 417500 },
  { id: 17, vehicleNo: 'C017', brand: '奔驰', model: 'GLC', year: 2022, price: 498000, mileage: 8000, color: '曜岩黑', transmission: '自动', displacement: '2.0T', city: '宁波', status: 1, isSpecial: true, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WDC2539561F123017', description: '豪华SUV，几乎全新', registrationDate: '2022-08-30', insuranceExpiry: '2027-08-29', purchasePrice: 445000, refurbishCost: 1000, finalCost: 446000 },
  { id: 18, vehicleNo: 'C018', brand: '奥迪', model: 'A6L', year: 2020, price: 358000, mileage: 55000, color: '神秘黑', transmission: '自动', displacement: '2.0T', city: '合肥', status: 0, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'WAUZZZ4G9BN123018', description: '商务旗舰，后排豪华', registrationDate: '2020-03-22', insuranceExpiry: '2026-03-21', purchasePrice: 305000, refurbishCost: 4000, finalCost: 309000 },
  { id: 19, vehicleNo: 'C019', brand: '丰田', model: 'RAV4', year: 2021, price: 218000, mileage: 33000, color: '超白珍珠', transmission: '自动', displacement: '2.5L', city: '福州', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'JTMRJREV1MD123019', description: '荣放双擎，省油耐用', registrationDate: '2021-06-10', insuranceExpiry: '2026-06-09', purchasePrice: 185000, refurbishCost: 2200, finalCost: 187200 },
  { id: 20, vehicleNo: 'C020', brand: '本田', model: '奥德赛', year: 2020, price: 248000, mileage: 42000, color: '月光银', transmission: '自动', displacement: '2.4L', city: '厦门', status: 1, isSpecial: false, coverImageUrl: placeholder(60, 45), images: [{ url: placeholder(400, 300), isMain: true }], vin: 'JHMRC1850LC123020', description: '商务MPV，魔术座椅', registrationDate: '2020-11-18', insuranceExpiry: '2026-11-17', purchasePrice: 210000, refurbishCost: 3000, finalCost: 213000 },
]
