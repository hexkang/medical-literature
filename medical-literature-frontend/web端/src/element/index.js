import { Menu,
        MenuItem,
        Submenu,
        Upload,
        Input,
        Icon,
        Button,
        Container,
        Header,
        Aside,
        Footer,
        Main,
        Pagination,
        Form,
        FormItem,
        Dialog
    } from 'element-ui';

const element = {
    install: function (Vue) {
        Vue.use(Menu),
        Vue.use(MenuItem),
        Vue.use(Submenu),
        Vue.use(Upload),
        Vue.use(Input),
        Vue.use(Icon),
        Vue.use(Button),
        Vue.use(Container),
        Vue.use(Header),
        Vue.use(Aside),
        Vue.use(Footer),
        Vue.use(Main),
        Vue.use(Pagination),
        Vue.use(Form),
        Vue.use(FormItem),
        Vue.use(Dialog)
    }
}

export default element
