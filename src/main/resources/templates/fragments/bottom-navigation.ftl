<!-- Bottom navigation fragment (mobile only) -->
<div class="md:hidden fixed bottom-0 left-0 right-0 bg-white shadow-lg border-t border-gray-100 z-50">
    <nav class="flex items-center justify-around px-2 py-2">
        <!-- Сотрудники -->
        <a href="${springMacroRequestContext.contextPath}/employees/"
           class="flex flex-col items-center justify-center px-2 py-1 text-gray-600 hover:text-sky-900 hover:bg-gray-50 group relative">
            <span class="relative">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
            </span>
            <span class="text-[10px] mt-0.5">Сотрудники</span>
            <span class="absolute -top-8 left-1/2 -translate-x-1/2 bg-gray-800 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none whitespace-nowrap">
                Сотрудники
            </span>
        </a>

        <!-- Добавить -->
        <a href="${springMacroRequestContext.contextPath}/employees/new"
           class="flex flex-col items-center justify-center px-2 py-1 text-gray-600 hover:text-sky-900 hover:bg-gray-50 group relative">
            <span class="relative">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                </svg>
            </span>
            <span class="text-[10px] mt-0.5">Добавить</span>
            <span class="absolute -top-8 left-1/2 -translate-x-1/2 bg-gray-800 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none whitespace-nowrap">
                Добавить
            </span>
        </a>

        <!-- Найти -->
        <a href="${springMacroRequestContext.contextPath}/employees/filter_employees"
           class="flex flex-col items-center justify-center px-2 py-1 text-gray-600 hover:text-sky-900 hover:bg-gray-50 group relative">
            <span class="relative">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2" d="m21 21l-4.486-4.494M19 10.5a8.5 8.5 0 1 1-17 0a8.5 8.5 0 0 1 17 0Z"/>
                </svg>
            </span>
            <span class="text-[10px] mt-0.5">Найти</span>
            <span class="absolute -top-8 left-1/2 -translate-x-1/2 bg-gray-800 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none whitespace-nowrap">
                Найти
            </span>
        </a>

        <!-- Сотрудники (табл.) -->
        <a href="${springMacroRequestContext.contextPath}/employees/show_employees"
           class="flex flex-col items-center justify-center px-2 py-1 text-gray-600 hover:text-sky-900 hover:bg-gray-50 group relative">
            <span class="relative">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
            </span>
            <span class="text-[10px] mt-0.5">Список</span>
            <span class="absolute -top-8 left-1/2 -translate-x-1/2 bg-gray-800 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none whitespace-nowrap">
                Сотрудники (табл.)
            </span>
        </a>

        <!-- Должности -->
        <a href="${springMacroRequestContext.contextPath}/positions/"
           class="flex flex-col items-center justify-center px-2 py-1 text-gray-600 hover:text-sky-900 hover:bg-gray-50 group relative">
            <span class="relative">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path d="M20.5,4H18.71L16.85,2.15A.36.36,0,0,0,16.69,2a.43.43,0,0,0-.19,0h-9a.43.43,0,0,0-.19,0,.36.36,0,0,0-.16.11L5.29,4H3.5A1.5,1.5,0,0,0,2,5.5v13A1.5,1.5,0,0,0,3.5,20H10l1.6,1.83a.51.51,0,0,0,.76,0L14,20H20.5A1.5,1.5,0,0,0,22,18.5V5.5A1.5,1.5,0,0,0,20.5,4ZM12.29,8h-.58l-1.5-1.5.5-.5h2.58l.5.5Zm1-3H10.71l-2-2h6.58Zm.92.5L16.5,3.21,17.79,4.5,15.5,6.79ZM7.5,3.21,9.79,5.5,8.5,6.79,6.21,4.5ZM3.5,19a.5.5,0,0,1-.5-.5V5.5A.5.5,0,0,1,3.5,5H5.29L8.15,7.85a.48.48,0,0,0,.7,0l.65-.64,1.43,1.43L8,17.34a.51.51,0,0,0,.09.49l1,1.17ZM12,20.74,9.06,17.39,11.86,9h.28l2.8,8.39Zm9-2.24a.5.5,0,0,1-.5.5H14.85l1-1.17a.51.51,0,0,0,.09-.49l-2.9-8.7L14.5,7.21l.65.64a.48.48,0,0,0,.7,0L18.71,5H20.5a.5.5,0,0,1,.5.5Z"/>
                </svg>
            </span>
            <span class="text-[10px] mt-0.5">Должности</span>
            <span class="absolute -top-8 left-1/2 -translate-x-1/2 bg-gray-800 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none whitespace-nowrap">
                Должности
            </span>
        </a>
    </nav>
</div>
<!-- END of the bottom navigation fragment -->
