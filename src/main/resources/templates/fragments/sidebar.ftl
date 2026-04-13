<!-- Sidebar fragment -->
<div id="sidebar" class="bg-white shadow-lg flex flex-col border-r border-gray-100 transition-all duration-300 overflow-hidden relative"
     x-data="sidebar()"
     :class="{ 'w-64': open, 'w-16': !open }">

    <!-- Logo -->
    <div class="px-4 py-4 text-center border-b border-gray-100 flex items-center justify-center logo-padding">
        <div class="w-8 h-8 bg-sky-900 text-white flex items-center justify-center font-bold rounded">
            HR
        </div>
        <span x-show="open" class="ml-3 text-2xl font-semibold text-sky-900 whitespace-nowrap">
            Отдел кадров
        </span>
    </div>

    <!-- Navigation -->
    <nav class="mt-4 flex-1 px-2">
        <a href="${springMacroRequestContext.contextPath}/employees/"
           class="flex items-center px-3 py-3 text-gray-700 font-medium transition rounded-none hover:bg-gray-200 hover:text-gray-700">
            <svg fill="#000000" class="h-5 w-5" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                <g id="SVGRepo_iconCarrier">
                    <title>employee_group_solid</title>
                    <g id="ad30ea0b-4044-46a8-9d02-5476e64acf86" data-name="Layer 3">
                        <ellipse cx="18" cy="11.28" rx="4.76" ry="4.7"></ellipse>
                        <path d="M10.78,11.75c.16,0,.32,0,.48,0,0-.15,0-.28,0-.43a6.7,6.7,0,0,1,3.75-6,4.62,4.62,0,1,0-4.21,6.46Z"></path>
                        <path d="M24.76,11.28c0,.15,0,.28,0,.43.16,0,.32,0,.48,0A4.58,4.58,0,1,0,21,5.29,6.7,6.7,0,0,1,24.76,11.28Z"></path>
                        <path d="M22.29,16.45a21.45,21.45,0,0,1,5.71,2,2.71,2.71,0,0,1,.68.53H34V15.56a.72.72,0,0,0-.38-.64,18,18,0,0,0-8.4-2.05l-.66,0A6.66,6.66,0,0,1,22.29,16.45Z"></path>
                        <path d="M6.53,20.92A2.76,2.76,0,0,1,8,18.47a21.45,21.45,0,0,1,5.71-2,6.66,6.66,0,0,1-2.27-3.55l-.66,0a18,18,0,0,0-8.4,2.05.72.72,0,0,0-.38.64V22H6.53Z"></path>
                        <rect x="21.46" y="26.69" width="5.96" height="1.4"></rect>
                        <path d="M32.81,21.26H25.94v-1a1,1,0,0,0-2,0v1H22V18.43A20.17,20.17,0,0,0,18,18a19.27,19.27,0,0,0-9.06,2.22.76.76,0,0,0-.41.68v5.61h7.11v6.09a1,1,0,0,0,1,1H32.81a1,1,0,0,0,1-1V22.26A1,1,0,0,0,32.81,21.26Zm-1,10.36H17.64V23.26h6.3v.91a1,1,0,0,0,2,0v-.91h5.87Z"></path>
                    </g>
                </g>
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Сотрудники</span>
        </a>
        <a href="${springMacroRequestContext.contextPath}/employees/new"
           class="flex items-center px-3 py-3 text-gray-700 font-medium transition rounded-none hover:bg-gray-200 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Добавить</span>
        </a>

        <!-- Filter Button -->
        <a href="${springMacroRequestContext.contextPath}/employees/filter"
           class="flex items-center px-3 py-3 text-gray-700 font-medium transition rounded-none hover:bg-gray-200 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <title>Найти сотрудников</title>
                <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2" d="m21 21l-4.486-4.494M19 10.5a8.5 8.5 0 1 1-17 0a8.5 8.5 0 0 1 17 0Z"/>
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Найти</span>
        </a>

        <a href="${springMacroRequestContext.contextPath}/employees/show_employees"
           class="flex items-center px-3 py-3 text-gray-700 font-medium transition rounded-none hover:bg-gray-200 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Сотрудники (табл.)</span>
        </a>

        <a href="${springMacroRequestContext.contextPath}/positions/"
           class="flex items-center px-3 py-3 text-gray-700 font-medium transition rounded-none hover:bg-gray-200 hover:text-gray-700">
           <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path d="M20.5,4H18.71L16.85,2.15A.36.36,0,0,0,16.69,2a.43.43,0,0,0-.19,0h-9a.43.43,0,0,0-.19,0,.36.36,0,0,0-.16.11L5.29,4H3.5A1.5,1.5,0,0,0,2,5.5v13A1.5,1.5,0,0,0,3.5,20H10l1.6,1.83a.51.51,0,0,0,.76,0L14,20H20.5A1.5,1.5,0,0,0,22,18.5V5.5A1.5,1.5,0,0,0,20.5,4ZM12.29,8h-.58l-1.5-1.5.5-.5h2.58l.5.5Zm1-3H10.71l-2-2h6.58Zm.92.5L16.5,3.21,17.79,4.5,15.5,6.79ZM7.5,3.21,9.79,5.5,8.5,6.79,6.21,4.5ZM3.5,19a.5.5,0,0,1-.5-.5V5.5A.5.5,0,0,1,3.5,5H5.29L8.15,7.85a.48.48,0,0,0,.7,0l.65-.64,1.43,1.43L8,17.34a.51.51,0,0,0,.09.49l1,1.17ZM12,20.74,9.06,17.39,11.86,9h.28l2.8,8.39Zm9-2.24a.5.5,0,0,1-.5.5H14.85l1-1.17a.51.51,0,0,0,.09-.49l-2.9-8.7L14.5,7.21l.65.64a.48.48,0,0,0,.7,0L18.71,5H20.5a.5.5,0,0,1,.5.5Z"/>
           </svg>
           <span x-show="open" class="ml-3 whitespace-nowrap">Должности</span>
        </a>

        <!-- Collapse Sidebar Button -->
        <a type="button"
           @click="toggle()"
           class="flex items-center px-3 py-3 text-gray-700 font-medium transition rounded-none hover:bg-gray-200 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 6H3m18 0l-4 4m4-4l-4-4M3 18h18M3 18l4 4m-4-4l4-4"/>
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Скрыть панель</span>
        </a>
    </nav>
</div>

<script>
document.addEventListener('alpine:init', () => {
    Alpine.data('sidebar', () => ({
        open: true,

        init() {
            // Восстанавливаем состояние из localStorage
            const saved = localStorage.getItem('sidebarOpen');
            this.open = saved ? JSON.parse(saved) : true;
        },

        toggle() {
            this.open = !this.open;
            // Сохраняем в localStorage
            localStorage.setItem('sidebarOpen', JSON.stringify(this.open));
        }
    }));
});
</script>
<!-- END of the sidebar fragment -->