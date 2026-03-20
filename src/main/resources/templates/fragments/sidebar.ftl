<div x-data="sidebarState()" class="bg-white shadow-lg flex flex-col border-r border-gray-100 transition-all duration-300 overflow-hidden relative"
     :class="open ? 'w-64' : 'w-16'">

    <!-- Logo -->
    <div class="p-4 text-center border-b border-gray-100 flex items-center justify-center">
        <div class="w-8 h-8 bg-sky-900 text-white flex items-center justify-center font-bold rounded">
            HR
        </div>
        <span x-show="open" class="ml-3 text-sm font-semibold text-sky-900 whitespace-nowrap">
            Отдел кадров
        </span>
    </div>

    <!-- Navigation -->
    <nav class="mt-4 flex-1 px-2">
        <a href="/"
           class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"/>
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Сотрудники</span>
        </a>
        <a href="/employees/new"
           class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Добавить</span>
        </a>
        <a href="/showEmployees"
           class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Показать всех</span>
        </a>

        <!-- Filter Button -->
        <a href="/filter"
           class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.207A1 1 0 013 6.5V4z" />
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Фильтр</span>
        </a>

        <!-- Collapse Sidebar Button -->
        <a href="#"
           @click="toggle"
           class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 6H3m18 0l-4 4m4-4l-4-4M3 18h18M3 18l4 4m-4-4l4-4"/>
            </svg>
            <span x-show="open" class="ml-3 whitespace-nowrap">Скрыть панель</span>
        </a>
    </nav>
</div>

<script>
    function sidebarState() {
        return {
            open: true,

            init() {
                // Читаем состояние из localStorage
                const saved = localStorage.getItem('sidebarOpen');
                if (saved !== null) {
                    this.open = JSON.parse(saved);
                } else {
                    this.open = true; // значение по умолчанию
                }
            },

            toggle() {
                this.open = !this.open;
                // Сохраняем в localStorage
                localStorage.setItem('sidebarOpen', JSON.stringify(this.open));
            },
        };
    }
</script>