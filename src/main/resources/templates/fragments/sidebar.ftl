        <!-- Toggle Button (Always visible) -->
        <button
            @click="sidebarOpen = !sidebarOpen"
            class="absolute top-4 -right-3 w-7 h-7 bg-white border border-gray-300 rounded-full shadow-md flex items-center justify-center z-10 hover:bg-sky-50 hover:border-sky-400 transition">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-sky-900" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      :d="sidebarOpen ? 'M15 19l-7-7 7-7' : 'M9 5l7 7-7 7'" />
            </svg>
        </button>

<#-- Sidebar Fragment -->
<div class="w-64 bg-white shadow-lg flex flex-col border-r border-gray-100">
        <!-- Logo -->
        <div class="p-4 text-center border-b border-gray-100 flex items-center justify-center">
            <div class="w-8 h-8 bg-sky-900 text-white flex items-center justify-center font-bold rounded">
                HR
            </div>
            <span
                x-show="sidebarOpen"
                class="ml-3 text-sm font-semibold text-sky-900 whitespace-nowrap transition-opacity duration-300">
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
                <span x-show="sidebarOpen" class="ml-3 whitespace-nowrap">Сотрудники</span>
            </a>
            <a href="/employees/new"
               class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6m0 0v6m0-6h6m-6 0H6"/>
                </svg>
                <span x-show="sidebarOpen" class="ml-3 whitespace-nowrap">Добавить</span>
            </a>
            <a href="/showEmployees"
               class="flex items-center px-3 py-3 text-gray-700 mb-2 font-medium transition rounded-none hover:bg-sky-50 hover:text-sky-900">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
                <span x-show="sidebarOpen" class="ml-3 whitespace-nowrap">Показать всех</span>
            </a>
        </nav>
    </div>
</div>