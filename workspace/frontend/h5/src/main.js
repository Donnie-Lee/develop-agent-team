import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import 'lib-flexible'
import 'vant/lib/index.css'
import './styles/common.scss'

// 按需引入所有使用的 Vant 组件
import {
  Tabbar, TabbarItem,
  Icon,
  Tag,
  Button,
  Image,
  Badge,
  CellGroup,
  Field,
  Uploader,
  Popup,
  ActionSheet,
  Checkbox
} from 'vant'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// 注册所有 Vant 组件
app.use(Tabbar)
app.use(TabbarItem)
app.use(Icon)
app.use(Tag)
app.use(Button)
app.use(Image)
app.use(Badge)
app.use(CellGroup)
app.use(Field)
app.use(Uploader)
app.use(Popup)
app.use(ActionSheet)
app.use(Checkbox)

app.mount('#app')
