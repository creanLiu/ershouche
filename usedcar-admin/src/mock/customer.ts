export interface Customer {
  id: number
  name: string
  phone: string
  wechat: string
  source: string
  // 1=高意向 2=已他购 3=已成交
  status: 1 | 2 | 3
  createdAt: string
  followupCount: number
  lastFollowupAt: string
  nextFollowupAt: string
  reminderNote: string
  salesId: string   // 负责销售的账号username
  salesName: string // 负责销售的显示名
}

export const customers: Customer[] = [
  { id: 1, name: '张伟', phone: '138****8001', wechat: 'zhangwei01', source: '线上', status: 1, createdAt: '2026-01-05', followupCount: 3, lastFollowupAt: '2026-02-10', nextFollowupAt: '2026-03-01', reminderNote: '约好下周到店看途观L', salesId: 'admin', salesName: '超级管理员' },
  { id: 2, name: '李娜', phone: '139****8002', wechat: 'lina02', source: '转介绍', status: 1, createdAt: '2026-01-08', followupCount: 5, lastFollowupAt: '2026-02-15', nextFollowupAt: '2026-02-28', reminderNote: '预算20万，关注凯美瑞', salesId: 'admin', salesName: '超级管理员' },
  { id: 3, name: '王芳', phone: '137****8003', wechat: 'wangfang03', source: '门店', status: 3, createdAt: '2026-01-10', followupCount: 2, lastFollowupAt: '2026-02-08', nextFollowupAt: '', reminderNote: '', salesId: 'admin', salesName: '超级管理员' },
  { id: 4, name: '刘洋', phone: '136****8004', wechat: 'liuyang04', source: '线上', status: 1, createdAt: '2026-01-12', followupCount: 4, lastFollowupAt: '2026-02-18', nextFollowupAt: '2026-03-05', reminderNote: '对宝马3系感兴趣，等降价', salesId: 'admin', salesName: '超级管理员' },
  { id: 5, name: '陈静', phone: '135****8005', wechat: 'chenjing05', source: '电话', status: 2, createdAt: '2026-01-15', followupCount: 1, lastFollowupAt: '2026-01-20', nextFollowupAt: '', reminderNote: '', salesId: 'admin', salesName: '超级管理员' },
  { id: 6, name: '赵磊', phone: '134****8006', wechat: 'zhaolei06', source: '转介绍', status: 1, createdAt: '2026-01-18', followupCount: 6, lastFollowupAt: '2026-02-20', nextFollowupAt: '2026-03-10', reminderNote: '家用SUV，预算30万内', salesId: 'admin', salesName: '超级管理员' },
  { id: 7, name: '孙丽', phone: '133****8007', wechat: 'sunli07', source: '门店', status: 3, createdAt: '2026-01-20', followupCount: 3, lastFollowupAt: '2026-02-12', nextFollowupAt: '', reminderNote: '', salesId: 'admin', salesName: '超级管理员' },
  { id: 8, name: '周强', phone: '132****8008', wechat: 'zhouqiang08', source: '线上', status: 1, createdAt: '2026-01-22', followupCount: 2, lastFollowupAt: '2026-02-05', nextFollowupAt: '2026-03-02', reminderNote: '考虑奔驰C级，需要试驾', salesId: 'admin', salesName: '超级管理员' },
  { id: 9, name: '吴敏', phone: '131****8009', wechat: 'wumin09', source: '电话', status: 1, createdAt: '2026-01-25', followupCount: 4, lastFollowupAt: '2026-02-22', nextFollowupAt: '2026-03-08', reminderNote: '首次购车，需要详细讲解', salesId: 'admin', salesName: '超级管理员' },
  { id: 10, name: '郑华', phone: '130****8010', wechat: 'zhenghua10', source: '转介绍', status: 3, createdAt: '2026-01-28', followupCount: 7, lastFollowupAt: '2026-02-23', nextFollowupAt: '', reminderNote: '', salesId: 'admin', salesName: '超级管理员' },
  { id: 11, name: '冯涛', phone: '158****8011', wechat: 'fengtao11', source: '门店', status: 1, createdAt: '2026-02-01', followupCount: 1, lastFollowupAt: '2026-02-03', nextFollowupAt: '2026-03-15', reminderNote: '看了奥迪A4L，需要考虑', salesId: 'admin', salesName: '超级管理员' },
  { id: 12, name: '蒋雪', phone: '159****8012', wechat: 'jiangxue12', source: '线上', status: 2, createdAt: '2026-02-03', followupCount: 2, lastFollowupAt: '2026-02-10', nextFollowupAt: '', reminderNote: '', salesId: 'admin', salesName: '超级管理员' },
  { id: 13, name: '韩鹏', phone: '150****8013', wechat: 'hanpeng13', source: '电话', status: 1, createdAt: '2026-02-05', followupCount: 3, lastFollowupAt: '2026-02-19', nextFollowupAt: '2026-03-20', reminderNote: '商务用车，关注帕萨特', salesId: 'admin', salesName: '超级管理员' },
  { id: 14, name: '杨梅', phone: '151****8014', wechat: 'yangmei14', source: '转介绍', status: 3, createdAt: '2026-02-08', followupCount: 5, lastFollowupAt: '2026-02-21', nextFollowupAt: '', reminderNote: '', salesId: 'admin', salesName: '超级管理员' },
  { id: 15, name: '许浩', phone: '152****8015', wechat: 'xuhao15', source: '门店', status: 1, createdAt: '2026-02-10', followupCount: 2, lastFollowupAt: '2026-02-24', nextFollowupAt: '2026-03-12', reminderNote: '年轻用户，关注思域', salesId: 'admin', salesName: '超级管理员' },
]
